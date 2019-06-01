package org.donntu.knt.mskit.course;


public class Main {
    public static void main(String[] args) throws Exception {
        //JPEGDecoder.decode("files/image.jpeg");
        /*byte[] bytes = new byte[16];
        for (int i = 0; i < 16; i++) {
            bytes[i] = (byte) (i + 1);
        }
        JPEGDecoder.createQuantizationTable(bytes);*/

        /*BufferedImage decode = JpegDecoder.decode(new File("files/p.jpg").toPath());
        int[][] pixels = ImageUtils.convertToPixels(decode);
        BitmapU bitmapU = new BitmapU();
        bitmapU.saveBitmap("files/new_pinya.bmp", decode, decode.getWidth(), decode.getHeight());

         */
        GaussianBlur.blur(null, 3);
    }

}
