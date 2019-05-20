package org.donntu.knt.mskit.course.v2.ddumanskiy.huffman;


import org.donntu.knt.mskit.course.v2.ddumanskiy.MCUBlockHolder;
import org.donntu.knt.mskit.course.v2.ddumanskiy.segments.SOFComponent;
import org.donntu.knt.mskit.course.v2.ddumanskiy.segments.SOSComponent;
import org.donntu.knt.mskit.course.v2.ddumanskiy.utils.ArraysUtil;
import org.donntu.knt.mskit.course.v2.ddumanskiy.utils.BitInputStream;
import org.donntu.knt.mskit.course.v2.ddumanskiy.utils.JpegInputStream;

import java.io.IOException;
import java.util.Arrays;

import static org.donntu.knt.mskit.course.v2.ddumanskiy.utils.BitUtil.first4Bits;
import static org.donntu.knt.mskit.course.v2.ddumanskiy.utils.BitUtil.last4Bits;


/**
 * Decodes input jpeg image byte array and produces minimum coded unit (MCU) per 1 HuffmanDecoder.decode() call.
 * For jpeg with 3 sosComponents MCU consists of 4 Y 8x8 arrays, 1 Cr 8x8 array and 1 Cb 8x8 array.
 *
 * User: ddumanskiy
 * Date: 8/13/2014
 * Time: 9:36 AM
 */
public class HuffmanDecoder {

    private HuffmanTree[] dcTables;
    private HuffmanTree[] acTables;
    private SOFComponent[] sofComponents;
    private SOSComponent[] sosComponents;
    private int[] block;
    private BitInputStream bitStream;

    public HuffmanDecoder(JpegInputStream jis, HuffmanTree[] dcTables, HuffmanTree[] acTables, SOFComponent[] sofComponents, SOSComponent[] sosComponents) {
        this.bitStream = new BitInputStream(jis.getIs());
        this.dcTables = dcTables;
        this.acTables = acTables;
        this.sofComponents = sofComponents;
        this.sosComponents = sosComponents;
        //will be used for reusage
        this.block = new int[ArraysUtil.SIZE * ArraysUtil.SIZE];
    }

    /**
     * Decodes input byte array producing 1 MCU for call.
     * Throws IllegalStateException when end of input is reached (right now I don't see better way to do that).
     *
     */
    public void decode(MCUBlockHolder holder) throws IOException {
        for (SOSComponent component : sosComponents) {
            SOFComponent sofComponent = sofComponents[component.getId() - 1];
            for (int i = 0; i < sofComponent.getVh(); i++) {
                int[] mcu = decode(dcTables[component.getDcDhtTableId()], acTables[component.getAcDhtTableId()]);
                holder.add(component.getId(), mcu, i);
            }
        }
    }

    /**
     * Decodes single 8x8 array either Y, either Cr, either Cb.
     */
    private int[] decode(HuffmanTree dcTable, HuffmanTree acTable) throws IOException {
        Arrays.fill(block, 0);
        block[0] = decodeDC(dcTable);
        decodeAC(acTable);
        return block;
    }

    /**
     * Finds huffman code in huffman tree. Algorithm is pretty simple :
     * - Read 1 bit;
     * - go down (to left node in case read bit is 0 and to right node in case read bit is 1) through huffman tree starting from root;
     * - if current node has filled code (code > -1) we found huffman code. otherwise - repeat.
     *
     */
    private int findCode(HuffmanTree table) throws IOException {
        HuffmanTree.Node start = table.root;

        for (int counter = 0; counter < 16; counter++) {
            int i = bitStream.nextBit();
            start = i == 1 ? start.node1 : start.node0;

            if (start.code > -1) {
                return start.code;
            }
        }

        throw new IllegalStateException("end of stream");
    }

    /**
     * Decodes single DC value. Algorithm is next :
     * - find huffman code for current bit position
     * -
     *    a) if code is 0 return 0
     *    b) if code starts from 1 return code itself
     *    c) if code starts from 0 calculate returned value by: code - 2^length_in_bits_of_code + 1
     *
     */
    public int decodeDC(HuffmanTree dcTable) throws IOException {
        int value = findCode(dcTable);
        return decodeCode(bitStream, value);
    }

    /**
     * Decodes rest of 63 AC values
     */
    private void decodeAC(HuffmanTree acTable) throws IOException{
        for (int k = 1; k < ArraysUtil.SIZE * ArraysUtil.SIZE; k++) {
            int code = findCode(acTable);
            int zerosNumber = first4Bits(code);
            int bitCount = last4Bits(code);

            if (bitCount != 0) {
                k += zerosNumber;
                block[k] = decodeCode(bitStream, bitCount);
            } else {
                if (zerosNumber != 0x0F) {
                    return;
                }
                k += 0x0F;
            }
        }
    }

    private static int decodeCode(BitInputStream bits, int bitCount) throws IOException {
        if (bitCount == 0) return 0;

        int firstBit = bits.nextBit();
        int val = 0;
        val = (val << 1) + firstBit;

        for (int i = 1; i < bitCount; i++) {
            val = (val << 1) + bits.nextBit();
        }

        if (firstBit == 1) {
            return val;
        } else {
            return val - (1 << bitCount) + 1;
        }
    }

}
