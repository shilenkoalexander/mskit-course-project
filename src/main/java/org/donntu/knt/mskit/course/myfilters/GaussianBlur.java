package org.donntu.knt.mskit.course.myfilters;

import java.awt.*;

import static java.lang.Math.*;

public class GaussianBlur {

    public static int[][] blur(int[][] pixels, int radius, double sigma) {
        int normalHeight = pixels.length;
        int normalWidth = pixels[0].length;
        int[][] newPixels = new int[normalHeight][normalWidth];
        int blurMatrixSize = 2 * radius + 1;

        double[][] blurMatrix = getMatrixByGaussian(blurMatrixSize, sigma);

        int sideSizeExtension = (int) Math.ceil(blurMatrixSize / 2);
        int[][] extendMatrix = extendMatrix(pixels, sideSizeExtension);
        for (int i = 0; i < normalHeight; i++) {
            for (int j = 0; j < normalWidth; j++) {
                newPixels[i][j] = (int) getBlurredPixel(
                        extendMatrix,
                        blurMatrix,
                        i + sideSizeExtension,
                        j + sideSizeExtension,
                        radius
                );
            }
        }

        return newPixels;
    }

    private static double getBlurredPixel(int[][] extendMatrix, double[][] blurMatrix, int i, int j, int radius) {
        double sumR = 0;
        double sumG = 0;
        double sumB = 0;
        for (int k = i - radius, p = 0; k < i + radius; k++, p++) {
            for (int l = j - radius, m = 0; l < j + radius; l++, m++) {
                Color color = new Color(extendMatrix[k][l]);
                sumR += (color.getRed() * blurMatrix[p][m]);
                sumG += (color.getGreen() * blurMatrix[p][m]);
                sumB += (color.getBlue() * blurMatrix[p][m]);
            }
        }

        return new Color((int)Math.round(sumR), (int)Math.round(sumG), (int)Math.round(sumB)).getRGB();
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
                System.out.printf("%4.3f ", blurMatrix[i][j]);
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
        return (1.0 / (2.0 * PI * pow(sigma, 2)) * exp(-(pow(x, 2) + pow(y, 2)) / (2 * pow(sigma, 2))));
    }
}
