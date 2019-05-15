package org.donntu.knt.mskit.course;

import org.donntu.knt.mskit.course.my.JpegDecoder;

public class Main {
    public static void main(String[] args) throws Exception {
        JpegDecoder.decode("files/image.jpeg");
        /*byte[] bytes = new byte[16];
        for (int i = 0; i < 16; i++) {
            bytes[i] = (byte) (i + 1);
        }
        JpegDecoder.createQuantizationTable(bytes);*/
    }

}
