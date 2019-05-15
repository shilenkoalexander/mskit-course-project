package org.donntu.knt.mskit.course.my;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import static org.donntu.knt.mskit.course.my.NumberUtil.bytesToHex;

public class JpegDecoder {
    private static final String FILE_START_MARKER = "FFD8";
    private static final String COMMENT_START_MARKER = "FFFE";
    private static final String QUANT_START_MARKER = "FFDB";

    public static JpegImage decode(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            JpegImage jpegImage = new JpegImage();
            try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
                String hexTemp;
                byte[] bytes = readBytes(file, 2); // file start
                checkMarkerEquals(bytesToHex(bytes), FILE_START_MARKER);

                skip(file, 2); // comment start

                bytes = readBytes(file, 2); // comment length
                int commentLength = ByteBuffer.wrap(bytes).getShort() - 2;

                if (commentLength > 0) {
                    bytes = readBytes(file, commentLength); //comment
                    jpegImage.setComment(new String(bytes));
                }


                bytes = readBytes(file, 2); // quant start
                checkMarkerEquals(bytesToHex(bytes), QUANT_START_MARKER);

                bytes = readBytes(file, 2); // quant length
                int quantLength = ByteBuffer.wrap(bytes).getShort();
                skip(file, 1);

                bytes = readBytes(file, quantLength); //quants
                createQuantizationTable(bytes);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return jpegImage;
        } else {
            throw new IllegalArgumentException("Ты че дурак. сказал же JPEG или JPG");
        }
    }

    private static byte[] readBytes(RandomAccessFile file, int count) throws IOException {
        byte[] bytes = new byte[count];
        file.read(bytes);
        return bytes;
    }

    private static void skip(RandomAccessFile file, int count) throws IOException {
        byte[] bytes = new byte[count];
        file.read(bytes);
    }

    private static void checkMarkerEquals(String fileMarker, String rightMarker) throws Exception {
        if (!fileMarker.equalsIgnoreCase(rightMarker)) {
            throw new Exception("Файл поврежден");
        }
    }

    public static byte[][] createQuantizationTable(byte[] bytes) {
        int tableSize = 8;
        byte[][] table = new byte[tableSize][tableSize];
        int k = 0;
        int i = 0;
        int j = 0;
        boolean up = true;
        while (k < bytes.length) {
            table[i][j] = bytes[k++];
            if (up) {
                if (i == 0 || j == tableSize - 1) {
                    up = false;
                } else {
                    i--;
                }
                if(j != tableSize - 1) {
                    j++;
                } else {
                    i++;
                }
            } else {
                if (i == tableSize - 1 || j == 0) {
                    up = true;
                } else {
                    j--;
                }
                if(i != tableSize - 1) {
                    i++;
                } else {
                    j++;
                }
            }
        }

        for (int l = 0; l < tableSize; l++) {
            for (int m = 0; m < tableSize; m++) {
                System.out.printf("%4d", (int)table[l][m]);
            }
            System.out.println();
        }
        return table;
    }
}
