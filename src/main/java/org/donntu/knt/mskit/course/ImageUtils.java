package org.donntu.knt.mskit.course;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static int[][] convertToPixels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = image.getRGB(col, row);
            }
        }

        return result;
    }

    public static BufferedImage convertToBI(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int r = (pixels[i][j]>>16)&0xFF;
                int g = (pixels[i][j]>>8)&0xFF;
                int b = (pixels[i][j]>>0)&0xFF;
                bufferedImage.setRGB(j, i, new Color(r, g, b).getRGB());
            }
        }

        return bufferedImage;
    }
}
