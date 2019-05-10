package org.donntu.knt.mskit.course;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

public class JpegWriter {
    public static final int DEFAULT_QUALITY = 75;

    public void saveAsJpeg(byte[] pixels, int width, int height, String path, int quality) {
        int biType = BufferedImage.TYPE_INT_RGB;
        BufferedImage bi = new BufferedImage(width, height, biType);
        try {
            Graphics g = bi.createGraphics();
            g.drawBytes(pixels, 0, pixels.length, 0, 0);
            g.dispose();
            Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = (ImageWriter) iter.next();
            File f = new File(path);
            String originalPath = null;
            boolean replacing = f.exists();
            if (replacing) {
                originalPath = path;
                path += ".temp";
                f = new File(path);
            }
            ImageOutputStream ios = ImageIO.createImageOutputStream(f);
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality / 100f);
            if (quality == 100)
                param.setSourceSubsampling(1, 1, 0, 0);
            IIOImage iioImage = new IIOImage(bi, null, null);
            writer.write(null, iioImage, param);
            ios.close();
            writer.dispose();
            if (replacing) {
                File f2 = new File(originalPath);
                boolean ok = f2.delete();
                if (ok) f.renameTo(f2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
