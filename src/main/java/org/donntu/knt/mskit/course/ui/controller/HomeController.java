package org.donntu.knt.mskit.course.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

public class HomeController {
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
    private TextField radiusTextField;

    @FXML
    private TextField sigmaTextField;

    @FXML
    private TextField filePathTextView;

    @FXML
    private RadioButton smoothingRadiobutton;

    @FXML
    void initialize() {

    }

}
