package org.donntu.knt.mskit.course.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.donntu.knt.mskit.course.Main;
import org.donntu.knt.mskit.course.ui.model.ImageModel;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static org.donntu.knt.mskit.course.image.ImageUtils.convertToImage;

public class HomeController {
    private ImageModel model = new ImageModel();

    @FXML
    private Button openFileDialogButton;

    @FXML
    private ImageView processedImageView;

    @FXML
    private RadioButton gaussianRadiobutton;

    @FXML
    private ToggleGroup filterGroup;

    @FXML
    private ImageView originalImageView;

    @FXML
    private Button processButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextField radiusTextField;

    @FXML
    private TextField sigmaTextField;

    @FXML
    private TextField filePathTextView;

    @FXML
    private RadioButton smoothingRadiobutton;

    @FXML
    void initialize() {
        Stage currentStage = Main.getCurrentStage();
        smoothingRadiobutton.setOnAction(event -> sigmaTextField.setDisable(true));
        gaussianRadiobutton.setOnAction(event -> sigmaTextField.setDisable(false));
        saveButton.setDisable(true);
        processButton.setDisable(true);
        gaussianRadiobutton.fire();
        setTextFieldsValidation();

        openFileDialogButton.setOnAction(event -> {
            String filename = model.openFileDialog(currentStage);
            if (filename != null) {
                filePathTextView.setText(filename);
                originalImageView.setImage(convertToImage(model.getOriginalPixels()));
                saveButton.setDisable(true);
                processButton.setDisable(false);
                processedImageView.setImage(null);
            }
        });

        processButton.setOnAction(event -> {
            String radius = radiusTextField.getText();
            Image image;
            if (filterGroup.getSelectedToggle().equals(gaussianRadiobutton)) {
                String sigma = sigmaTextField.getText();
                image = model.blurImage(Integer.valueOf(radius), Double.valueOf(sigma));
            } else {
                image = model.smoothImage(Integer.valueOf(radius));
            }
            processedImageView.setImage(image);
            saveButton.setDisable(false);
        });

        saveButton.setOnAction(event -> {
            if (filterGroup.getSelectedToggle().equals(gaussianRadiobutton)) {
                model.saveBlurImage();
            } else {
                model.saveSmoothImage();
            }
        });
    }

    private void setTextFieldsValidation() {
        Pattern validEditingState = Pattern.compile("(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                if (!text.isEmpty() && Double.valueOf(text) > 15.0) {
                    return null;
                }
                return c;
            } else {
                return null;
            }
        };

        StringConverter<Double> converter = new StringConverter<Double>() {

            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || ".".equals(s)) {
                    return 0.0;
                } else {
                    Double value = Double.valueOf(s);
                    if (value > 15.0) {
                        value = 15.0;
                    }
                    return value;
                }
            }


            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };

        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 2.0, filter);
        sigmaTextField.setTextFormatter(textFormatter);

        Pattern validEditingState1 = Pattern.compile("(([1-9][0-9]*)|0)?");

        filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState1.matcher(text).matches()) {
                if (!text.isEmpty() && Integer.valueOf(text) > 20) {
                    return null;
                }
                return c;
            } else {
                return null;
            }
        };

        StringConverter<Integer> converter1 = new StringConverter<Integer>() {

            @Override
            public Integer fromString(String s) {
                if (s.isEmpty() || ".".equals(s)) {
                    return 0;
                } else {
                    Integer value = Integer.valueOf(s);
                    if (value > 20) {
                        value = 20;
                    }
                    return value;
                }
            }


            @Override
            public String toString(Integer d) {
                return d.toString();
            }
        };

        TextFormatter<Integer> textFormatter1 = new TextFormatter<>(converter1, 5, filter);
        radiusTextField.setTextFormatter(textFormatter1);
    }

}
