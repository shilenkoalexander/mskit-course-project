package org.donntu.knt.mskit.course;

import static java.lang.Math.*;

public class GaussianBlur {
    private static double NORMING_FACTOR = 1;

    public static int[][] blur(int[][] pixels, int radius, double sigma) {
        int normalHeight = pixels.length;
        int normalWidth = pixels[0].length;
        int[][] newPixels = new int[normalHeight][normalWidth];

        double[][] blurMatrix = getMatrixByGaussian(radius, sigma);

        int sideSizeExtension = radius / 2;
        int[][] extendMatrix = extendMatrix(pixels, sideSizeExtension);
        for (int i = 0; i < normalHeight; i++) {
            for (int j = 0; j < normalWidth; j++) {
                newPixels[i][j] = (int) getBlurredPixel(extendMatrix, blurMatrix, i + sideSizeExtension, j + sideSizeExtension, radius);
            }
        }

        return newPixels;
    }

    private static double getBlurredPixel(int[][] extendMatrix, double[][] blurMatrix, int i, int j, int radius) {
        int half = radius / 2;
        double sum = 0;
        for (int k = i - half, p = 0; k < i + half; k++, p++) {
            for (int l = j - half, m = 0; l < j + half; l++, m++) {
                sum += (extendMatrix[k][l] * blurMatrix[p][m]) / NORMING_FACTOR;
            }
        }
        return sum;
    }

    private static int[][] extendMatrix(int[][] matrix, int sideSizeExtension) {
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

    private static double[][] getMatrixByGaussian(int size, double sigma) {
        double[][] blurMatrix = new double[size][size];
        int half = size / 2;
        double sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blurMatrix[i][j] = getGaussianValue(i - half, j - half, sigma);
                sum += blurMatrix[i][j];
                System.out.printf("%3.2f ", blurMatrix[i][j]);
            }
            System.out.println();
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blurMatrix[i][j] /= sum;
            }
        }

        return blurMatrix;
    }

    private static double getGaussianValue(double x, double y, double sigma) {
        return (1.0 / (2.0 * PI * pow(sigma, 2)) * exp(-(pow(x, 2) + pow(y, 2)) / 2 * pow(sigma, 2)));
    }

    private static double multiplyMatrix(double[][] matrix1, double[][] matrix2) {
        double sum = 0;
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[i].length; j++) {
                sum += matrix1[i][j] * matrix2[i][j];
            }
        }
        return sum;
    }
}
