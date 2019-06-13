package org.donntu.knt.mskit.course.image;

public class MatrixUtils {
    public static int[][] extendMatrix(int[][] matrix, int sideSizeExtension) {
        int matrixHeight = matrix.length;
        int matrixWidth = matrix[0].length;
        int[][] extendedMatrix = new int[matrixHeight + sideSizeExtension * 2][matrixWidth + sideSizeExtension * 2];
        int oldI = 0;
        int oldJ = 0;
        for (int i = 0; i < extendedMatrix.length; i++) {
            if(i <= sideSizeExtension){
                oldI = 0;
            } else if(i >= sideSizeExtension + matrixHeight) {
                oldI = matrixHeight - 1;
            } else {
                oldI++;
            }
            for (int j = 0; j < extendedMatrix[i].length; j++) {
                if(j <= sideSizeExtension){
                    oldJ = 0;
                } else if(j >= sideSizeExtension + matrixWidth) {
                    oldJ = matrixWidth - 1;
                } else {
                    oldJ++;
                }
                extendedMatrix[i][j] = matrix[oldI][oldJ];

            }
        }

        return extendedMatrix;
    }

    public static int[][] vectorToMatrix(int[] vector, int width, int height) {
        int[][] matrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            System.arraycopy(vector, i * width, matrix[i], 0, width);
        }
        return matrix;
    }
}
