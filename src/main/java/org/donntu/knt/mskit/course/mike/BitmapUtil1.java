package org.donntu.knt.mskit.course.mike;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
public class BitmapUtil1 {
    private char[] header = new char[2];
    private byte[] head = new byte[2];
    private int size;
    private int reserved1;
    private int reserved2;
    private int offset;
    private int headerSize;
    private int bitmapWidth;
    private int bitmapHeight;
    private int numColorPlanes;
    private int numBitsPixel;
    private int compressionMethod;
    private int imageSize;
    private int horRes;
    private int vertRes;
    private int numColorPalette;
    private int numImportantColors;
    private int padding;
    private int[] pixels;
    private BufferedImage image;

    public BitmapUtil1() {
    }

    public BitmapUtil1(BitmapUtil1 pic) {
        header = pic.header;
        head = pic.head;
        size = pic.size;
        reserved1 = pic.reserved1;
        reserved2 = pic.reserved2;
        offset = pic.offset;
        headerSize = pic.headerSize;
        bitmapWidth = pic.bitmapWidth;
        bitmapHeight = pic.bitmapHeight;
        numColorPlanes = pic.numColorPlanes;
        numBitsPixel = pic.numBitsPixel;
        compressionMethod = pic.compressionMethod;
        imageSize = pic.imageSize;
        horRes = pic.horRes;
        vertRes = pic.vertRes;
        numColorPalette = pic.numColorPalette;
        numImportantColors = pic.numImportantColors;
        padding = pic.padding;
        pixels = pic.pixels;
        image = new BufferedImage(bitmapWidth, bitmapHeight, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < bitmapHeight; x++) {
            for (int y = 0; y < bitmapWidth; y++) {
                pixels[x * bitmapWidth + y] = pic.pixels[x * bitmapWidth + y];
            }
        }
        for (int x = 0; x < bitmapWidth; x++) {
            for (int y = 0; y < bitmapHeight; y++) {
                image.setRGB(x, y, pic.getPixel(x, y).getRGB());
            }
        }
    }

    public void loadBMP(String fileName) {
        try (FileInputStream fs = new FileInputStream(fileName)) {
            int bflen = 14;
            byte[] bf = new byte[bflen];
            fs.read(bf, 0, bflen);
            int bilen = 40;
            byte[] bi = new byte[bilen];
            fs.read(bi, 0, bilen);
            size = (((int) bf[5] & 0xff) << 24)
                    | (((int) bf[4] & 0xff) << 16)
                    | (((int) bf[3] & 0xff) << 8)
                    | (int) bf[2] & 0xff;
            reserved1 = ((((int) bf[7] & 0xff) << 8)
                    | (int) bf[6] & 0xff);
            reserved2 = ((((int) bf[9] & 0xff) << 8)
                    | (int) bf[8] & 0xff);
            offset = (((int) bf[13] & 0xff) << 24)
                    | (((int) bf[12] & 0xff) << 16)
                    | (((int) bf[11] & 0xff) << 8)
                    | (int) bf[10] & 0xff;
            header[0] = (char) bf[0];
            header[1] = (char) bf[1];
            head[0] = bf[0];
            head[1] = bf[1];
            headerSize = (((int) bi[3] & 0xff) << 24)
                    | (((int) bi[2] & 0xff) << 16)
                    | (((int) bi[1] & 0xff) << 8)
                    | (int) bi[0] & 0xff;
            bitmapWidth = (((int) bi[7] & 0xff) << 24)
                    | (((int) bi[6] & 0xff) << 16)
                    | (((int) bi[5] & 0xff) << 8)
                    | (int) bi[4] & 0xff;
            bitmapHeight = (((int) bi[11] & 0xff) << 24)
                    | (((int) bi[10] & 0xff) << 16)
                    | (((int) bi[9] & 0xff) << 8)
                    | (int) bi[8] & 0xff;
            numColorPlanes = ((((int) bi[13] & 0xff) << 8) | (int) bi[12] & 0xff);
            numBitsPixel = ((((int) bi[15] & 0xff) << 8) | (int) bi[14] & 0xff);
            compressionMethod = (((int) bi[19]) << 24)
                    | (((int) bi[18]) << 16)
                    | (((int) bi[17]) << 8)
                    | (int) bi[16];
            imageSize = (((int) bi[23] & 0xff) << 24)
                    | (((int) bi[22] & 0xff) << 16)
                    | (((int) bi[21] & 0xff) << 8)
                    | (int) bi[20] & 0xff;
            if (getImageSize() == 0) {
                imageSize = 3 * getBitmapWidth() + getBitmapWidth() % 4;
                imageSize = getImageSize() * getBitmapHeight();
            }
            horRes = (((int) bi[27] & 0xff) << 24)
                    | (((int) bi[26] & 0xff) << 16)
                    | (((int) bi[25] & 0xff) << 8)
                    | (int) bi[24] & 0xff;
            vertRes = (((int) bi[31] & 0xff) << 24)
                    | (((int) bi[30] & 0xff) << 16)
                    | (((int) bi[29] & 0xff) << 8)
                    | (int) bi[28] & 0xff;
            numColorPalette = (((int) bi[35] & 0xff) << 24)
                    | (((int) bi[34] & 0xff) << 16)
                    | (((int) bi[33] & 0xff) << 8)
                    | (int) bi[32] & 0xff;
            numImportantColors = (((int) bi[39] & 0xff) << 24)
                    | (((int) bi[38] & 0xff) << 16)
                    | (((int) bi[37] & 0xff) << 8)
                    | (int) bi[36] & 0xff;
            if (numBitsPixel == 24) {
                padding = (getImageSize() / getBitmapHeight()) - getBitmapWidth() * 3;
                pixels = new int[getBitmapHeight() * getBitmapWidth()];
                byte[] brgb = new byte[(getBitmapWidth() + getPadding()) * 3 * getBitmapHeight()];
                fs.read(brgb, 0, (getBitmapWidth() + getPadding()) * 3 * getBitmapHeight());
                int nindex = 0;
                for (int j = 0; j < getBitmapHeight(); j++) {
                    for (int i = 0; i < getBitmapWidth(); i++) {
                        int a = (0xff) << 24;
                        int r = (((int) brgb[nindex + 2] & 0xff) << 16);
                        int g = (((int) brgb[nindex + 1] & 0xff) << 8);
                        int b = (int) brgb[nindex] & 0xff;
                        pixels[getBitmapWidth() * (getBitmapHeight() - j - 1) + i] = a + b + g + r;
                        nindex += 3;
                    }
                    nindex += getPadding();
                }
            } else if (numBitsPixel == 32) {
                padding = 0;
                pixels = new int[getBitmapHeight() * getBitmapWidth()];
                byte[] brgb = new byte[getBitmapWidth() * 4 * getBitmapHeight()];
                fs.read(brgb, 0, getBitmapWidth() * 4 * getBitmapHeight());
                int nindex = 0;
                for (int j = 0; j < getBitmapHeight(); j++) {
                    for (int i = 0; i < getBitmapWidth(); i++) {
                        pixels[getBitmapWidth() * (getBitmapHeight() - j - 1) + i] =
                                (((int) brgb[nindex + 3] & 0xff) << 24)
                                        | (((int) brgb[nindex + 2] & 0xff) << 16)
                                        | (((int) brgb[nindex + 1] & 0xff) << 8)
                                        | (int) brgb[nindex] & 0xff;
                        nindex += 4;
                    }
                }
            }
            Toolkit tk = Toolkit.getDefaultToolkit();
            Image images = tk.createImage(new MemoryImageSource(getBitmapWidth(), getBitmapHeight(), pixels, 0, getBitmapWidth()));
            this.image = new BufferedImage(getBitmapWidth(), getBitmapHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = this.image.createGraphics();
            g2.drawImage(images, 0, 0, null);
            g2.dispose();
        } catch (IOException ex) {
            Logger.getLogger(BitmapUtil1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printBMPHeader() {
        System.out.println("=============================");
        System.out.println("Новое изображение:");
        System.out.println("Header Field: " + header[0] + header[1]);
        System.out.println("Size of BMP File: " + size);
        System.out.println("Reserved(First): " + reserved1);
        System.out.println("Reserved(Second): " + reserved2);
        System.out.println("Offset: " + offset);
        System.out.println("Size of the header: " + headerSize);
        System.out.println("Bitmap width: " + getBitmapWidth());
        System.out.println("Bitmap height: " + getBitmapHeight());
        System.out.println("Number Of Color Planes: " + numColorPlanes);
        System.out.println("Number of bits per pixel: " + numBitsPixel);
        System.out.println("Compression Method: " + compressionMethod);
        System.out.println("Image Size: " + getImageSize());
        System.out.println("Horizontal Resolution of Image: " + horRes);
        System.out.println("Vertical Resolution of Image: " + vertRes);
        System.out.println("Number of colors in Color Palette: " + numColorPalette);
        System.out.println("Number of Important Colors used: " + numImportantColors);
    }

    public Color getPixel(int x, int y) {
        if (x < 0 || x >= bitmapWidth) {
            throw new IndexOutOfBoundsException("x must be between 0 and " + (bitmapWidth - 1));
        }
        if (y < 0 || y >= bitmapHeight) {
            throw new IndexOutOfBoundsException("y must be between 0 and " + (bitmapHeight - 1));
        }
        return new Color(image.getRGB(x, y));
    }

    public void setPixel(int x, int y, Color color) {
        if (x < 0 || x >= bitmapWidth) {
            throw new IndexOutOfBoundsException("x must be between 0 and " + (bitmapWidth - 1));
        }
        if (y < 0 || y >= bitmapHeight) {
            throw new IndexOutOfBoundsException("y must be between 0 and " + (bitmapHeight - 1));
        }
        if (color == null) {
            throw new NullPointerException("can't set Color to null");
        }
        image.setRGB(x, y, color.getRGB());
    }

    public double getLuminance(int x, int y) {
        Color color = getPixel(x, y);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return .299 * r + .587 * g + .114 * b;
    }

    public void saveToFile(String file) {
        try (FileOutputStream fo = new FileOutputStream(file)) {
            fo.write(head);
            fo.write(intToDWord(size));
            fo.write(intToWord(reserved1));
            fo.write(intToWord(reserved2));
            fo.write(intToDWord(offset));
            fo.write(intToDWord(headerSize));
            fo.write(intToDWord(bitmapWidth));
            fo.write(intToDWord(bitmapHeight));
            fo.write(intToWord(numColorPlanes));
            fo.write(intToWord(numBitsPixel));
            fo.write(intToDWord(compressionMethod));
            fo.write(intToDWord(0));
            fo.write(intToDWord(horRes));
            fo.write(intToDWord(vertRes));
            fo.write(intToDWord(numColorPalette));
            fo.write(intToDWord(numImportantColors));
            writeBitmap(fo);
        } catch (IOException ex) {
            Logger.getLogger(BitmapUtil1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private byte[] intToWord(int parValue) {
        byte[] retValue = new byte[2];
        retValue[0] = (byte) (parValue & 0xFF);
        retValue[1] = (byte) ((parValue >> 8) & 0xFF);
        return (retValue);
    }

    private byte[] intToDWord(int parValue) {
        byte[] retValue = new byte[4];
        retValue[0] = (byte) (parValue & 0xFF);
        retValue[1] = (byte) ((parValue >> 8) & 0xFF);
        retValue[2] = (byte) ((parValue >> 16) & 0xFF);
        retValue[3] = (byte) ((parValue >> 24) & 0xFF);
        return (retValue);
    }

    private boolean convertImage() {
        PixelGrabber pg = new PixelGrabber(image, 0, 0, bitmapWidth, bitmapHeight,
                pixels, 0, bitmapWidth);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return (false);
        }
        return (true);
    }

    private void writeBitmap(FileOutputStream fo) {
        convertImage();
        int value;
        int pad;
        byte[] rgb;
        if (numBitsPixel == 24) {
            pad = 4 - ((bitmapWidth * 3) % 4);
            rgb = new byte[3];
        } else {
            rgb = new byte[4];
            pad = 4 - ((bitmapWidth) % 4);
        }
        if (pad == 4) {
            pad = 0;
        }
        try {
            for (int row = bitmapHeight; row > 0; row--) {
                for (int col = 0; col < bitmapWidth; col++) {
                    value = pixels[(row - 1) * bitmapWidth + col];
                    if (numBitsPixel == 24) {
                        rgb[0] = (byte) (value & 0xFF);
                        rgb[1] = (byte) ((value >> 8) & 0xFF);
                        rgb[2] = (byte) ((value >> 16) & 0xFF);
                    }
                    if (numBitsPixel == 32) {
                        rgb[0] = (byte) (value & 0xFF);
                        rgb[1] = (byte) ((value >> 8) & 0xFF);
                        rgb[2] = (byte) ((value >> 16) & 0xFF);
                        rgb[3] = (byte) ((value >> 24) & 0xFF);
                    }
                    fo.write(rgb);
                    for (int i = 1; i <= pad; i++) {
                        fo.write(0x00);
                    }
                }
            }
        } catch (Exception wb) {
            wb.printStackTrace();
        }
    }
}

