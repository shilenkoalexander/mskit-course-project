package org.donntu.knt.mskit.course.image;

import org.donntu.knt.mskit.course.image.kernel.KernelValue;

import java.awt.*;

import static org.donntu.knt.mskit.course.image.MatrixUtils.extendMatrix;

public class Filter {

    public int[][] process(int[][] pixels, int radius, KernelValue kernelValue) {
        int normalHeight = pixels.length;
        int normalWidth = pixels[0].length;
        int[][] newPixels = new int[normalHeight][normalWidth];
        int blurMatrixSize = 2 * radius + 1;

        double[] blurKernel = getKernel(blurMatrixSize, kernelValue);

        int[][] extendMatrix = extendMatrix(pixels, radius);

        for (int i = 0; i < normalHeight; i++) {
            for (int j = 0; j < normalWidth; j++) {
                extendMatrix[i + radius][j + radius] = getBlurredPixel(
                        extendMatrix,
                        blurKernel,
                        i + radius,
                        j + radius,
                        radius,
                        false
                );
            }
        }

        for (int i = 0; i < normalHeight; i++) {
            for (int j = 0; j < normalWidth; j++) {
                newPixels[i][j] = getBlurredPixel(
                        extendMatrix,
                        blurKernel,
                        i + radius,
                        j + radius,
                        radius,
                        true
                );
            }
        }
        return newPixels;
    }


    private int getBlurredPixel(int[][] extendMatrix, double[] blurKernel, int i, int j, int radius, boolean vertical) {
        double sumR = 0;
        double sumG = 0;
        double sumB = 0;
        int m = vertical ? i : j;
        for (int k = m - radius, p = 0; k < m + radius + 1; k++, p++) {
            int rgb;
            if (vertical) {
                rgb = extendMatrix[k][j];
            } else {
                rgb = extendMatrix[i][k];
            }
            Color color = new Color(rgb);
            sumR += (color.getRed() * blurKernel[p]);
            sumG += (color.getGreen() * blurKernel[p]);
            sumB += (color.getBlue() * blurKernel[p]);
        }

        return new Color((int) Math.round(sumR), (int) Math.round(sumG), (int) Math.round(sumB)).getRGB();
    }

    private double[] getKernel(int size, KernelValue kernelValue) {
        double[] blurKernel = new double[size];
        int half = size / 2;
        double sum = 0;
        for (int i = 0; i < size; i++) {
            double rowSum = 0.0;
            for (int j = 0; j < size; j++) {
                rowSum += kernelValue.getValue(i - half, j - half);
            }
            blurKernel[i] = rowSum;
            sum += rowSum;
        }

        for (int i = 0; i < size; i++) {
            blurKernel[i] /= sum;
        }
        return blurKernel;
    }
}
