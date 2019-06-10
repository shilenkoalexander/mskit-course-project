package org.donntu.knt.mskit.course.myfilters;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

final class LittleEndianDataOutput implements AutoCloseable {
    private DataOutputStream out;

    public LittleEndianDataOutput(OutputStream out) {
        this.out = new DataOutputStream(out);
    }

    public void writeBytes(byte[] b) throws IOException {
        out.write(b);
    }

    // The top 16 bits are ignored
    public void writeInt16(int x) throws IOException {
        out.writeShort((x & 0xFF) << 8 | (x & 0xFF00) >>> 8);
    }

    public void writeInt32(int x) throws IOException {
        out.writeInt(Integer.reverseBytes(x));
    }

    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws Exception {
        if(out != null) {
            out.close();
        }
    }
}
