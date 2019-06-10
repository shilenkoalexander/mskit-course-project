package org.donntu.knt.mskit.course;


import org.donntu.knt.mskit.course.jpegreader.JpegDecoder;
import org.donntu.knt.mskit.course.myfilters.BMPWriter;
import org.donntu.knt.mskit.course.myfilters.Filter;
import org.donntu.knt.mskit.course.myfilters.kernel.GaussianKernelValue;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting...");
        int[][] pixels = JpegDecoder.decode(new File("files/cat2.jpg").toPath());
        System.out.println("Decoded");

        Filter filter = new Filter();
        int radius = 5;
        double sigma = 3;

        int[][] blurred = filter.process(pixels, radius, new GaussianKernelValue(sigma));

        /*BufferedImage bufferedImage = ImageUtils.convertToBI(blurred);
        ImageIO.write(bufferedImage, "bmp", new File("files/blurred.bmp"));
*/
        System.out.println("Blurred");
        BMPWriter bmpWriter = new BMPWriter();
        bmpWriter.save(blurred,"files/blurred.bmp");
        System.out.println("Saved");
    }

}
