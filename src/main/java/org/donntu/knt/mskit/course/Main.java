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

   /* public static void main(String[] args) throws Exception {
        System.out.println("Starting...");
        int[][] pixels = JpegDecoder.decode(new File("files/cat.jpg").toPath());
        System.out.println("Decoded");

        Filter filter = new Filter();
        int radius = 5;
        double sigma = 3;

        int[][] blurred = filter.process(pixels, radius, new GaussianKernelValue(sigma));

        System.out.println("Blurred");
        BMPWriter bmpWriter = new BMPWriter();
        bmpWriter.save(blurred,"files/blurred.bmp");
        System.out.println("Saved");
    }*/

}
