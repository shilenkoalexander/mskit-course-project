package org.donntu.knt.mskit.course.image.jpegreader;


import org.donntu.knt.mskit.course.image.MatrixUtils;
import org.donntu.knt.mskit.course.image.jpegreader.huffman.HuffmanDecoder;
import org.donntu.knt.mskit.course.image.jpegreader.merger.ColorBlockMerger;
import org.donntu.knt.mskit.course.image.jpegreader.merger.GrayBlockMerger;
import org.donntu.knt.mskit.course.image.jpegreader.merger.Merger;
import org.donntu.knt.mskit.course.image.jpegreader.segments.*;
import org.donntu.knt.mskit.course.image.jpegreader.utils.ArraysUtil;
import org.donntu.knt.mskit.course.image.jpegreader.utils.DCT;
import org.donntu.knt.mskit.course.image.jpegreader.utils.JpegInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * User: ddumanskiy
 * Date: 8/13/2014
 * Time: 9:36 AM
 */
public class JpegDecoder {

    public static int[][] decode(Path jpegImageFilePath) throws IOException {
        //buffering full byte array for speedup.
        int fileSize = (int) Files.size(jpegImageFilePath);

        try (JpegInputStream jis = new JpegInputStream(new BufferedInputStream(Files.newInputStream(jpegImageFilePath), fileSize))) {

            SegmentHolder segmentHolder = readAllSegments(jis);

            HuffmanDecoder huffmanDecoder = new HuffmanDecoder(
                    jis,
                    segmentHolder.dhtSegment.getDcTables(),
                    segmentHolder.dhtSegment.getAcTables(),
                    segmentHolder.sofSegment.getComponents(),
                    segmentHolder.sosSegment.getComponents()
            );

            MCUBlockHolder holder = new MCUBlockHolder();
            Merger merger;
            if (segmentHolder.sofSegment.getComponentCount() == 1) {
                merger = new GrayBlockMerger(segmentHolder.sofSegment.getWidth(), segmentHolder.sofSegment.getHeight());
            } else {
                merger = new ColorBlockMerger(segmentHolder.sofSegment.getWidth(), segmentHolder.sofSegment.getHeight(), segmentHolder.sofSegment);
            }


            while (true) {
                try {
                    huffmanDecoder.decode(holder);
                    multiplyAll(segmentHolder.dqtSegment, holder, segmentHolder.sofSegment.getComponentCount());
                    fillInZigZagOrder(holder, segmentHolder.sofSegment.getComponentCount());
                    inverseDCTAll(holder, segmentHolder.sofSegment.getComponentCount());
                    merger.merge(holder);
                } catch (IllegalStateException e) {
                    break;
                }
            }
            return MatrixUtils.vectorToMatrix(
                    merger.getFullBlock(),
                    segmentHolder.sofSegment.getWidth(),
                    segmentHolder.sofSegment.getHeight()
            );
        }

    }

    private static void fillInZigZagOrder(MCUBlockHolder holder, int compNum) {
        ArraysUtil.fillInZigZagOrder(holder.yComponentsZZ, holder.yComponents);
        if (compNum == 3) {
            ArraysUtil.fillInZigZagOrder(holder.cbComponentZZ, holder.cbComponent);
            ArraysUtil.fillInZigZagOrder(holder.crComponentZZ, holder.crComponent);
        }
    }

    private static void inverseDCTAll(MCUBlockHolder holder, int compNum) {
        DCT.inverseDCT(holder.yComponentsZZ);
        if (compNum == 3) {
            DCT.inverseDCT(holder.cbComponentZZ);
            DCT.inverseDCT(holder.crComponentZZ);
        }
    }

    private static void multiplyAll(DQTSegment dqtSegment, MCUBlockHolder holder, int compNum) {
        ArraysUtil.multiply(dqtSegment.getDqtTables()[0], holder.yComponents);
        if (compNum == 3) {
            ArraysUtil.multiply(dqtSegment.getDqtTables()[1], holder.cbComponent);
            ArraysUtil.multiply(dqtSegment.getDqtTables()[1], holder.crComponent);
        }
    }

    /**
     * Read all segments from a file and creates appropriate objects.
     *
     * @param jis - image file as byte stream.
     * @throws IOException
     */
    private static SegmentHolder readAllSegments(JpegInputStream jis) throws IOException {
        SegmentHolder segmentHolder = new SegmentHolder();

        while (segmentHolder.sosSegment == null) {
            int marker = jis.readShort();

            switch (marker) {
                case Markers.SOI:
                case Markers.EOI:
                    break;

                case Markers.SOF0:
                    segmentHolder.sofSegment = SOFSegment.decode(jis);
                    break;

                case Markers.SOF1:
                case Markers.SOF2:

                case Markers.DRI:

                case Markers.COM:

                case Markers.RST0:
                case Markers.RST1:
                case Markers.RST2:
                case Markers.RST3:
                case Markers.RST4:
                case Markers.RST5:
                case Markers.RST6:
                case Markers.RST7:

                case Markers.APP0:
                case Markers.APP1:
                case Markers.APP2:
                case Markers.APP3:
                case Markers.APP4:
                case Markers.APP5:
                case Markers.APP6:
                case Markers.APP7:
                case Markers.APP8:
                case Markers.APP9:
                case Markers.APPA:
                case Markers.APPB:
                case Markers.APPC:
                case Markers.APPD:
                case Markers.APPE:
                case Markers.APPF:
                    jis.skip(jis.readSize());
                    break;
                case Markers.DHT:
                    DHTTable dhtTable = DHTSegment.decode(jis);
                    segmentHolder.dhtSegment.add(dhtTable);
                    break;

                case Markers.DQT:
                    DQTTable dqtTable = DQTSegment.decode(jis);
                    segmentHolder.dqtSegment.add(dqtTable);
                    break;

                case Markers.SOS:
                    segmentHolder.sosSegment = SOSSegment.decode(jis);
                    break;
                default:
                    System.out.println("Should be never reached.");
                    break;
            }
        }

        return segmentHolder;
    }
}
