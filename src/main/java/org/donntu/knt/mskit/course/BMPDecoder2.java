package org.donntu.knt.mskit.course;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.InputStream;


public class BMPDecoder2 {
    private InputStream is;
    private int curPos = 0;

    private int bitmapOffset;               // starting position of image data

    private int width;                              // image width in pixels
    private int height;                             // image height in pixels
    private short bitsPerPixel;             // 1, 4, 8, or 24 (no color map)
    private int compression;                // 0 (none), 1 (8-bit RLE), or 2 (4-bit RLE)
    private int actualSizeOfBitmap;
    private int scanLineSize;
    private int actualColorsUsed;

    private byte[] r;
    private byte[] g;
    private byte[] b;             // color palette
    private int noOfEntries;

    private byte[] byteData;                // Unpacked data
    private int[] intData;                     // Unpacked data


    private int readInt() throws IOException {
        int b1 = is.read();
        int b2 = is.read();
        int b3 = is.read();
        int b4 = is.read();
        curPos += 4;
        return ((b4 << 24) + (b3 << 16) + (b2 << 8) + (b1));
    }


    private short readShort() throws IOException {
        int b1 = is.read();
        int b2 = is.read();
        curPos += 2;
        return (short) ((b2 << 8) + b1);
    }


    private void getFileHeader() throws Exception {
        // Actual contents (14 bytes):
        short fileType;// always "BM"
        int fileSize;                   // size of file in bytes
        short reserved1 = 0;    // always 0
        short reserved2 = 0;    // always 0

        fileType = readShort();
        if (fileType != 0x4d42) {
            throw new Exception("Not a BMP file");  // wrong file type
        }
        fileSize = readInt();
        reserved1 = readShort();
        reserved2 = readShort();
        bitmapOffset = readInt();
    }

    private void getBitmapHeader() throws IOException {

        // Actual contents (40 bytes):
        int size;                               // size of this header in bytes
        short planes;                   // no. of color planes: always 1
        int sizeOfBitmap;               // size of bitmap in bytes (may be 0: if so, calculate)
        int horzResolution;             // horizontal resolution, pixels/meter (may be 0)
        int vertResolution;             // vertical resolution, pixels/meter (may be 0)
        int colorsUsed;                 // no. of colors in palette (if 0, calculate)
        int colorsImportant;    // no. of important colors (appear first in palette) (0 means all are important)
        int noOfPixels;

        size = readInt();
        width = readInt();
        height = readInt();
        planes = readShort();
        bitsPerPixel = readShort();
        compression = readInt();
        sizeOfBitmap = readInt();
        horzResolution = readInt();
        vertResolution = readInt();
        colorsUsed = readInt();
        colorsImportant = readInt();
        if (bitsPerPixel == 24) {
            colorsUsed = colorsImportant = 0;
        }

        final boolean topDown = (height < 0);
        if (topDown) {
            height = -height;
        }
        noOfPixels = width * height;

        // Scan line is padded with zeroes to be a multiple of four bytes
        scanLineSize = ((width * bitsPerPixel + 31) / 32) * 4;

        actualSizeOfBitmap = scanLineSize * height;

        if (colorsUsed != 0) {
            actualColorsUsed = colorsUsed;
        } else {
            if (bitsPerPixel < 16) {
                actualColorsUsed = 1 << bitsPerPixel;
            } else {
                actualColorsUsed = 0;   // no palette
            }
        }


        System.out.println("BMP_Reader");
        System.out.println("  width: " + width);
        System.out.println("  height: " + height);
        System.out.println("  compression: " + compression);
        System.out.println("  scanLineSize: " + scanLineSize);
        System.out.println("  planes: " + planes);
        System.out.println("  bitsPerPixel: " + bitsPerPixel);
        System.out.println("  sizeOfBitmap: " + sizeOfBitmap);
        System.out.println("  horzResolution: " + horzResolution);
        System.out.println("  vertResolution: " + vertResolution);
        System.out.println("  colorsUsed: " + colorsUsed);
        System.out.println("  colorsImportant: " + colorsImportant);


    }

    private void getPalette() throws IOException {
        noOfEntries = actualColorsUsed;
        if (noOfEntries > 0) {
            r = new byte[noOfEntries];
            g = new byte[noOfEntries];
            b = new byte[noOfEntries];

            int reserved;
            for (int i = 0; i < noOfEntries; i++) {
                b[i] = (byte) is.read();
                g[i] = (byte) is.read();
                r[i] = (byte) is.read();
                reserved = is.read();
                curPos += 4;
            }
        }
    }

    private void unpack(byte[] rawData, int rawOffset, int bpp, byte[] byteData, int byteOffset, int w) throws Exception {
        int j = byteOffset;
        int k = rawOffset;
        byte mask;
        int pixPerByte;

        switch (bpp) {
            case 1:
                mask = (byte) 0x01;
                pixPerByte = 8;
                break;
            case 4:
                mask = (byte) 0x0f;
                pixPerByte = 2;
                break;
            case 8:
                mask = (byte) 0xff;
                pixPerByte = 1;
                break;
            default:
                throw new Exception("Unsupported bits-per-pixel value: " + bpp);
        }

        for (int i = 0; ; ) {
            int shift = 8 - bpp;
            for (int ii = 0; ii < pixPerByte; ii++) {
                byte br = rawData[k];
                br >>= shift;
                byteData[j] = (byte) (br & mask);
                //System.out.println("Setting byteData[" + j + "]=" + Test.byteToHex(byteData[j]));
                j++;
                i++;
                if (i == w) return;
                shift -= bpp;
            }
            k++;
        }
    }

    private void unpack24(byte[] rawData, int rawOffset, int[] intData, int intOffset, int w) {
        int j = intOffset;
        int k = rawOffset;
        int mask = 0xff;
        for (int i = 0; i < w; i++) {
            int b0 = (((int) (rawData[k++])) & mask);
            int b1 = (((int) (rawData[k++])) & mask) << 8;
            int b2 = (((int) (rawData[k++])) & mask) << 16;
            intData[j] = 0xff000000 | b0 | b1 | b2;
            j++;
        }
    }

    private void unpack32(byte[] rawData, int rawOffset, int[] intData, int intOffset, int w) {
        int j = intOffset;
        int k = rawOffset;
        int mask = 0xff;
        for (int i = 0; i < w; i++) {
            int b0 = (((int) (rawData[k++])) & mask);
            int b1 = (((int) (rawData[k++])) & mask) << 8;
            int b2 = (((int) (rawData[k++])) & mask) << 16;
            int b3 = (((int) (rawData[k++])) & mask) << 24; // this gets ignored!
            intData[j] = 0xff000000 | b0 | b1 | b2;
            j++;
        }
    }

    private void getPixelData() throws Exception {
        byte[] rawData;                 // the raw unpacked data

        // Skip to the start of the bitmap data (if we are not already there)
        long skip = bitmapOffset - curPos;
        if (skip > 0) {
            is.skip(skip);
            curPos += skip;
        }

        int len = scanLineSize;
        if (bitsPerPixel > 8) {
            intData = new int[width * height];
        } else {
            byteData = new byte[width * height];
        }
        rawData = new byte[actualSizeOfBitmap];
        int rawOffset = 0;
        int offset = (height - 1) * width;
        for (int i = height - 1; i >= 0; i--) {
            int n = is.read(rawData, rawOffset, len);
            if (n < len) {
                throw new Exception("Scan line ended prematurely after " + n + " bytes");
            }
            if (bitsPerPixel == 24) {
                unpack24(rawData, rawOffset, intData, offset, width);
            } else if (bitsPerPixel == 32) {
                unpack32(rawData, rawOffset, intData, offset, width);
            } else {
                unpack(rawData, rawOffset, bitsPerPixel, byteData, offset, width);
            }
            rawOffset += len;
            offset -= width;
        }
    }


    public void read(InputStream is) throws Exception {
        this.is = is;
        getFileHeader();
        getBitmapHeader();
        if (compression != 0) {
            throw new Exception("Compression not supported");
        }
        getPalette();
        getPixelData();
    }


    public MemoryImageSource makeImageSource() {
        ColorModel cm;
        MemoryImageSource mis;

        if (noOfEntries > 0 && bitsPerPixel != 24) {
            // There is a color palette; create an IndexColorModel
            cm = new IndexColorModel(bitsPerPixel, noOfEntries, r, g, b);
        } else {
            // There is no palette; use the default RGB color model
            cm = ColorModel.getRGBdefault();
        }

        // Create MemoryImageSource

        if (bitsPerPixel > 8) {
            // use one int per pixel
            mis = new MemoryImageSource(width, height, cm, intData, 0, width);
        } else {
            // use one byte per pixel
            mis = new MemoryImageSource(width, height, cm, byteData, 0, width);
        }
        return mis;      // this can be used by Component.createImage()
    }

    public int[] getIntData() {
        return intData;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
