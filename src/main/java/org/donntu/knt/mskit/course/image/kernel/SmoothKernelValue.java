package org.donntu.knt.mskit.course.image.kernel;

import static java.lang.Math.pow;

public class SmoothKernelValue implements KernelValue {
    private int radius;

    public SmoothKernelValue(int radius) {
        this.radius = radius;
    }

    @Override
    public double getValue(int i, int j) {
        return 1 / (pow(radius, 2) + 1);
    }
}
