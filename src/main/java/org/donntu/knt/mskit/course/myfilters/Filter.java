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

        double[] blurKernel = getKernel(blurMatrixSize, kernelValue);

        int[][] extendMatrix = extendMatrix(pixels, radius);
        for (int i = 0; i < normalHeight; i++) {
            for (int j = 0; j < normalWidth; j++) {
                for (int k = 0; k < 2; k++) {
                    newPixels[i][j] = getBlurredPixel(
                            extendMatrix,
                            blurKernel,
                            i + radius,
                            j + radius,
                            radius,
                            k == 0
                    );
                }
            }
        }

        return newPixels;
    }

    private int getBlurredPixel(int[][] extendMatrix, double[] blurKernel, int i, int j, int radius, boolean vertical) {
        double sumR = 0;
        double sumG = 0;
        double sumB = 0;
        for (int k = i - radius, p = 0; k < i + radius + 1; k++, p++) {
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

    /*private int getBlurredPixel(int[][] extendMatrix, double[] blurMatrix, int i, int j, int radius, boolean vertical) {
        double sumR = 0;
        double sumG = 0;
        double sumB = 0;
        for (int k = i - radius, p = 0; k < i + radius + 1; k++, p++) {
            for (int l = j - radius, m = 0; l < j + radius + 1; l++, m++) {
                Color color = new Color(extendMatrix[k][l]);
                sumR += (color.getRed() * blurMatrix[p][m]);
                sumG += (color.getGreen() * blurMatrix[p][m]);
                sumB += (color.getBlue() * blurMatrix[p][m]);
            }
        }

        return new Color((int) Math.round(sumR), (int) Math.round(sumG), (int) Math.round(sumB)).getRGB();
    }*/

    private double[] getKernel(int size, KernelValue kernelValue) {
        double[] blurKernel = new double[size];
        int half = size / 2;
        double sum = 0;
        for (int i = 0; i < size; i++) {
            double rowSum = 0.0;
            for (int j = 0; j < size; j++) {
                rowSum += kernelValue.getValue(i - half, j - half);
//                System.out.printf("%4.3f ", blurKernel[i][j]);
            }
            blurKernel[i] = rowSum;
            sum += rowSum;
//            System.out.println();
        }

        double sumNormal = 0;
        for (int i = 0; i < size; i++) {
            blurKernel[i] /= sum;
            sumNormal += blurKernel[i];
        }
        System.out.println("sum = " + sumNormal);
        return blurKernel;
    }
}
