package org.donntu.knt.mskit.course.v2.ddumanskiy;


import org.donntu.knt.mskit.course.v2.ddumanskiy.huffman.HuffmanDecoder;
import org.donntu.knt.mskit.course.v2.ddumanskiy.merger.ColorBlockMerger;
import org.donntu.knt.mskit.course.v2.ddumanskiy.merger.GrayBlockMerger;
import org.donntu.knt.mskit.course.v2.ddumanskiy.merger.Merger;
import org.donntu.knt.mskit.course.v2.ddumanskiy.segments.*;
import org.donntu.knt.mskit.course.v2.ddumanskiy.utils.ArraysUtil;
import org.donntu.knt.mskit.course.v2.ddumanskiy.utils.DCT;
import org.donntu.knt.mskit.course.v2.ddumanskiy.utils.JpegInputStream;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.donntu.knt.mskit.course.v2.ddumanskiy.Markers.*;
import static org.donntu.knt.mskit.course.v2.ddumanskiy.utils.ArraysUtil.multiply;

/**
 * User: ddumanskiy
 * Date: 8/13/2014
 * Time: 9:36 AM
 */
public class JpegDecoder {

    public static BufferedImage decode(Path jpegImageFilePath) throws IOException {
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

            try {
                while (true) {
                    huffmanDecoder.decode(holder);
                    multiplyAll(segmentHolder.dqtSegment, holder, segmentHolder.sofSegment.getComponentCount());
                    fillInZigZagOrder(holder, segmentHolder.sofSegment.getComponentCount());
                    inverseDCTAll(holder, segmentHolder.sofSegment.getComponentCount());
                    merger.merge(holder);
                }
            } catch (IllegalStateException ignored) { }

            return makeImageFromRGBMatrix(
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
        multiply(dqtSegment.getDqtTables()[0], holder.yComponents);
        if (compNum == 3) {
            multiply(dqtSegment.getDqtTables()[1], holder.cbComponent);
            multiply(dqtSegment.getDqtTables()[1], holder.crComponent);
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
                case SOI:
                    break;

                case SOF0:
                    segmentHolder.sofSegment = SOFSegment.decode(jis);
                    break;

                case SOF1:
                case SOF2:

                case DRI:

                case COM:

                case RST0:
                case RST1:
                case RST2:
                case RST3:
                case RST4:
                case RST5:
                case RST6:
                case RST7:

                case APP0:
                case APP1:
                case APP2:
                case APP3:
                case APP4:
                case APP5:
                case APP6:
                case APP7:
                case APP8:
                case APP9:
                case APPA:
                case APPB:
                case APPC:
                case APPD:
                case APPE:
                case APPF:
                    jis.skip(jis.readSize());
                    break;
                case DHT:
                    DHTTable dhtTable = DHTSegment.decode(jis);
                    segmentHolder.dhtSegment.add(dhtTable);
                    break;

                case DQT:
                    DQTTable dqtTable = DQTSegment.decode(jis);
                    segmentHolder.dqtSegment.add(dqtTable);
                    break;

                case SOS:
                    segmentHolder.sosSegment = SOSSegment.decode(jis);
                    break;
                case EOI:
                    break;
                default:
                    System.out.println("Should be never reached.");
                    break;
            }
        }

        return segmentHolder;
    }

    public static BufferedImage makeImageFromRGBMatrix(int[] rgbMatrix, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final int[] a = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        System.arraycopy(rgbMatrix, 0, a, 0, rgbMatrix.length);
        return bi;
    }
}
