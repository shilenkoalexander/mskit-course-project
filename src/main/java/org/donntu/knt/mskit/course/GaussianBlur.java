package org.donntu.knt.mskit.course;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class GaussianBlur {
    public static int[][] blur(int[][] pixels, int blurMatrixSize) {
        double[][] blurMatrix = getMatrixByGaussian(blurMatrixSize);
        return null;
    }

    private static double[][] getMatrixByGaussian(int size) {
        double[][] blurMatrix = new double[size][size];
        double sigma = 0.3 * (size / 2.0 - 1.0) + 0.8;
        int half = size / 2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blurMatrix[i][j] = exp(-(pow(i - half, 2) + pow(j - half, 2)) / (2.0 * sigma * sigma))
                        / (2.0 * Math.PI * sigma * sigma);
                System.out.printf("%8.5f ", blurMatrix[i][j]);
            }
            System.out.println();
        }
        /*int Half = (size * size) >> 1;
        double[] Weights = new double[size * size];
        //   Central weight
        Weights[Half] = 1.0;
        for (int Weight = 1; Weight < Half + 1; ++Weight) {
            //   Support point
            double x = 3.0 * (double) Weight / (double)Half;
            //   Corresponding symmetric weights
            Weights[Half - Weight] = Weights[Half + Weight] = exp(-x * x / 2.);
            System.out.printf("%8.5f ", Weights[Half - Weight]);
        }*/
        return blurMatrix;
    }
}
