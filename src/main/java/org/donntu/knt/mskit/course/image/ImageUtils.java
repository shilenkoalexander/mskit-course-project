package org.donntu.knt.mskit.course.image;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {
    public static Image convertToImage(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int r = (pixels[i][j] >> 16) & 0xFF;
                int g = (pixels[i][j] >> 8) & 0xFF;
                int b = (pixels[i][j] >> 0) & 0xFF;
                bufferedImage.setRGB(j, i, new Color(r, g, b).getRGB());
            }
        }

        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
}
