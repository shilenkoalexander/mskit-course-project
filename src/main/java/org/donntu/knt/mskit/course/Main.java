package org.donntu.knt.mskit.course;


import org.donntu.knt.mskit.course.v2.ddumanskiy.JpegDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {

        BufferedImage decode = JpegDecoder.decode(new File("files/p.jpg").toPath());
        int[][] pixels = ImageUtils.convertToPixels(decode);
        //BitmapU bitmapU = new BitmapU();
        //bitmapU.saveBitmap("files/new_pinya.bmp", decode, decode.getWidth(), decode.getHeight());

        /*for (int i = 3; i < 20; i++) {
            int[][] blurred = GaussianBlur.blur(pixels, 5, 2);
            ImageIO.write(ImageUtils.convertToBI(blurred), "bmp", new File("files/blur/blurred" + i + ".bmp"));
        }*/
        int[][] blurred = GaussianBlur.blur(pixels, 20, 1.5);
        ImageIO.write(ImageUtils.convertToBI(blurred), "bmp", new File("files/blurred.bmp"));
    }

}
