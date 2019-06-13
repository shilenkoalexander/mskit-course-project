package org.donntu.knt.mskit.course.image.jpegreader.segments;


import org.donntu.knt.mskit.course.image.jpegreader.utils.BitUtil;
import org.donntu.knt.mskit.course.image.jpegreader.utils.JpegInputStream;

import java.io.IOException;

/**
 * User: ddumanskiy
 * Date: 8/13/2014
 * Time: 9:36 AM
 */
public class DQTSegment {

    //todo seems 2 is a max?
    private int[][] dqtTables = new int[2][];

    public static DQTTable decode(JpegInputStream jis) throws IOException {
        int size = jis.readSize();

        int sizeAndId = jis.read();
        int sizeOfElement = BitUtil.first4Bits(sizeAndId);
        int id = BitUtil.last4Bits(sizeAndId);
        //for now ignore element size for simplicity. assume always 1 byte
        if (sizeOfElement > 1) {
            throw new IllegalStateException("Unexpected dqt element size.");
        }


        //because we read 1 byte already
        size -= 1;

        int[] dqtDataArray = new int[size];
        //todo read as a batch?
        for (int i = 0; i < size; i++) {
            dqtDataArray[i] = jis.read();
        }
        //HexDump.dump(markerData, 0, System.out, 0);
        return new DQTTable(sizeOfElement, id, dqtDataArray);
    }

    public int[][] getDqtTables() {
        return dqtTables;
    }

    public void add(DQTTable dqtTable) {
        dqtTables[dqtTable.getId()] = dqtTable.getData();
    }

}
