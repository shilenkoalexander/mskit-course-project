package org.donntu.knt.mskit.course;


import org.donntu.knt.mskit.course.myfilters.BMPWriter;
import org.donntu.knt.mskit.course.myfilters.Filter;
import org.donntu.knt.mskit.course.myfilters.ImageUtils;
import org.donntu.knt.mskit.course.myfilters.kernel.SmoothKernelValue;
import org.donntu.knt.mskit.course.jpegreader.JpegDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Starting...");
        BufferedImage decode = JpegDecoder.decode(new File("files/p.jpg").toPath());
        System.out.println("Decoded");
        int[][] pixels = ImageUtils.convertToPixels(decode);
        System.out.println("Converted");
        //BitmapU bitmapU = new BitmapU();
        //bitmapU.saveBitmap("files/new_pinya.bmp", decode, decode.getWidth(), decode.getHeight());

        /*for (int i = 3; i < 20; i++) {
            int[][] blurred = GaussianBlur.process(pixels, 5, 2);
            ImageIO.write(ImageUtils.convertToBI(blurred), "bmp", new File("files/process/blurred" + i + ".bmp"));
        }*/

        Filter filter = new Filter();
        int radius = 5;
        double sigma = 2;

        int[][] blurred = filter.process(pixels, radius, new SmoothKernelValue(radius));

        System.out.println("Blurred");
        //ImageIO.write(ImageUtils.convertToBI(blurred), "bmp", new File("files/blurred.bmp"));
        BMPWriter bmpWriter = new BMPWriter();
        bmpWriter.save(blurred,"files/blurred.bmp");
        System.out.println("Saved");
    }

}
