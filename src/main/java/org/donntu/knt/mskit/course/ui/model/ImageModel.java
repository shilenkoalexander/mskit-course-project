package org.donntu.knt.mskit.course.ui.model;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.donntu.knt.mskit.course.Main;
import org.donntu.knt.mskit.course.jpegreader.JpegDecoder;
import org.donntu.knt.mskit.course.myfilters.BMPWriter;
import org.donntu.knt.mskit.course.myfilters.Filter;
import org.donntu.knt.mskit.course.myfilters.kernel.GaussianKernelValue;
import org.donntu.knt.mskit.course.myfilters.kernel.SmoothKernelValue;

import java.io.File;
import java.io.IOException;

import static org.donntu.knt.mskit.course.myfilters.ImageUtils.convertToImage;

/**
 * @author Shilenko Alexander
 */
public class ImageModel {
    private int[][] originalPixels;
    private int[][] smoothPixels;
    private int[][] blurPixels;
    private Filter filter = new Filter();


    public int[][] getOriginalPixels() {
        return originalPixels;
    }

    public String openFileDialog(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Файлы",
                        "*.jpg", "*.jpeg"
                )
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                originalPixels = JpegDecoder.decode(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    private String openPathChooserDialog() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Bitmap Image (*.bmp)", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(Main.getCurrentStage());
        return file.getAbsolutePath();
    }

    public Image blurImage(Integer radius, Double sigma) {
        blurPixels = filter.process(originalPixels, radius, new GaussianKernelValue(sigma));
        return convertToImage(blurPixels);
    }

    public Image smoothImage(Integer radius) {
        smoothPixels = filter.process(originalPixels, radius, new SmoothKernelValue(radius));
        return convertToImage(smoothPixels);
    }

    public void saveBlurImage() {
        BMPWriter.save(blurPixels, openPathChooserDialog());
    }

    public void saveSmoothImage() {
        BMPWriter.save(smoothPixels, openPathChooserDialog());
    }
}
