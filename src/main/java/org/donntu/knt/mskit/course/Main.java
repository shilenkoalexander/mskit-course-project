package org.donntu.knt.mskit.course;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        BMPDecoder bmpDecoder = new BMPDecoder();
        bmpDecoder.read(new FileInputStream("files/blured.bmp"));
        Gaus gaus = new Gaus();
        int[] ints = gaus.processImage(bmpDecoder.getIntData(), bmpDecoder.getWidth(), bmpDecoder.getHeight());
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        ImageIO.write(image, "BMP", new File("filename.bmp"));


    }
}
