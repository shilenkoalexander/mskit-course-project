package org.donntu.knt.mskit.course.myfilters;

import org.donntu.knt.mskit.course.myfilters.kernel.KernelValue;

import java.awt.*;

import static org.donntu.knt.mskit.course.myfilters.MatrixUtils.extendMatrix;

public class Filter {

    public int[][] process(int[][] pixels, int radius, KernelValue kernelValue) {
        int normalHeight = pixels.length;
        int normalWidth = pixels[0].length;
        int[][] newPixels = new int[normalHeight][normalWidth];
        int blurMatrixSize = 2 * radius + 1;

        double[][] blurMatrix = getKernel(blurMatrixSize, kernelValue);

        int[][] extendMatrix = extendMatrix(pixels, radius);
        for (int i = 0; i < normalHeight; i++) {
            for (int j = 0; j < normalWidth; j++) {
                newPixels[i][j] = getBlurredPixel(
                        extendMatrix,
                        blurMatrix,
                        i + radius,
                        j + radius,
                        radius
                );
            }
        }

        return newPixels;
    }

    private int getBlurredPixel(int[][] extendMatrix, double[][] blurMatrix, int i, int j, int radius) {
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

    private double[][] getKernel(int size, KernelValue kernelValue) {
        double[][] blurMatrix = new double[size][size];
        int half = size / 2;
        double sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blurMatrix[i][j] = kernelValue.getValue(i - half, j - half);
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
}
