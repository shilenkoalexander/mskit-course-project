package org.donntu.knt.mskit.course.jpegreader.utils;


import java.io.IOException;
import java.io.InputStream;

public class BitInputStream {

    private InputStream is;

    private int currentByte;

    private int bitCounter;

    public BitInputStream(InputStream is) {
        this.is = is;
        currentByte = 0;
        bitCounter = 0;
    }

    public int nextBit() throws IOException {
        if (bitCounter == 0) {
            currentByte = is.read();
            bitCounter = 8;

            if (currentByte == 0xff) {
                final int b2 = is.read();
                if (b2 != 0) {
                    throw new IllegalStateException("end");
                }
            }
        }

        int bit = (currentByte >> 7) & 1;
        bitCounter--;
        currentByte = currentByte << 1;
        return bit;
    }

}
