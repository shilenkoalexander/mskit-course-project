package org.donntu.knt.mskit.course;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage currentStage = null;

    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        currentStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/home.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("МСКИТ");
        primaryStage.setMaxWidth(900);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
