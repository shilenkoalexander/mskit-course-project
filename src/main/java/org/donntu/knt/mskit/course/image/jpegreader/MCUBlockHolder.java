package org.donntu.knt.mskit.course.image.jpegreader;


import org.donntu.knt.mskit.course.image.jpegreader.utils.ArraysUtil;

/**
 * Class for holding single MCU unit. In case of model with 3 sof components it holds
 * 4 Y, 1 Cr, 1 Cb matrix. All matrix 8 x 8 size.
 *
 * User: ddumanskiy
 * Date: 8/13/2014
 * Time: 9:36 AM
 */
public class MCUBlockHolder {

    //I hold here both zigzag and not zigzagged for performance reasons,
    //so we do as much operations as we can with one dimension array
    //(as it gives benefits from perf. point of view) and when all is done
    //we convert not zigzagged vals to zigzag and than draw them.

    //zigzagged values
    public int[][][] yComponentsZZ = new int[4][ArraysUtil.SIZE][ArraysUtil.SIZE];
    public int[][] cbComponentZZ = new int[ArraysUtil.SIZE][ArraysUtil.SIZE];
    public int[][] crComponentZZ = new int[ArraysUtil.SIZE][ArraysUtil.SIZE];

    //not zigzagged values
    public int[][] yComponents = new int[4][ArraysUtil.SIZE * ArraysUtil.SIZE];
    public int[] cbComponent = new int[ArraysUtil.SIZE * ArraysUtil.SIZE];
    public int[] crComponent = new int[ArraysUtil.SIZE * ArraysUtil.SIZE];

    private int yDCPrev = 0;
    private int cbDCPrev = 0;
    private int crDCPrev = 0;

    public void add(int componentId, int[] mcu, int counter) {
        if (componentId == 1) {
            yDCPrev = incrDC(mcu, yDCPrev);
            System.arraycopy(mcu, 0, yComponents[counter], 0, ArraysUtil.SIZE * ArraysUtil.SIZE);
        }
        if (componentId == 2) {
            cbDCPrev = incrDC(mcu, cbDCPrev);
            System.arraycopy(mcu, 0, cbComponent, 0, ArraysUtil.SIZE * ArraysUtil.SIZE);
        }
        if (componentId == 3) {
            crDCPrev = incrDC(mcu,crDCPrev);
            System.arraycopy(mcu, 0, crComponent, 0, ArraysUtil.SIZE * ArraysUtil.SIZE);
        }
    }

    private static int incrDC(int[] data, int dcVal) {
        data[0] += dcVal;
        return data[0];
    }

}
