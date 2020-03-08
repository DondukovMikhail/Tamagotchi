package com.tamagotchi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private Parent root;
    private Stage currentStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/petChooseScene.fxml"));
        root = loader.load();

        currentStage = primaryStage;
        currentStage.setScene(new Scene(root));
        currentStage.setMinWidth(640);
        currentStage.setMinHeight(480);
        currentStage.setResizable(false);
        currentStage.setTitle("Tamagotchi");
        currentStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
