package com.tamagotchi;

import com.tamagotchi.controller.MainSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {
    private Parent root;
    private Stage currentStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        File file = new File("game.json");
        boolean isFirstLaunch = false;
        if (!file.exists()) {
            isFirstLaunch = true;
        }

        FXMLLoader loader;
        if (isFirstLaunch)
            loader = new FXMLLoader(getClass().getResource("/fxml/petChooseScene.fxml"));
        else
            loader = new FXMLLoader(getClass().getResource("/fxml/mainScene.fxml"));
        root = loader.load();

        currentStage = primaryStage;
        currentStage.setScene(new Scene(root));
        currentStage.setMinWidth(640);
        currentStage.setMinHeight(480);
        currentStage.setResizable(false);
        currentStage.setTitle("Tamagotchi");
        if (isFirstLaunch)
            currentStage.show();
        else {
            MainSceneController mainSceneController = loader.getController();
            currentStage.setOnCloseRequest(we -> mainSceneController.stop());
            currentStage.show();
            mainSceneController.initPet(0);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
