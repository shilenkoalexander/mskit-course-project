package org.donntu.knt.mskit.course.myfilters.kernel;

import static java.lang.Math.*;

public class GaussianKernelValue implements KernelValue {
    private double sigma;

    public GaussianKernelValue(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double getValue(int i, int j) {
            return (1.0 / (2.0 * PI * pow(sigma, 2)) * exp(-(pow(i, 2) + pow(j, 2)) / (2 * pow(sigma, 2))));
    }
}
