package com.tamagotchi;

import com.tamagotchi.controller.MainSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/mainScene.fxml"));
        //MainSceneController controller = loader.getController();
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(480);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Tamagotchi");
        primaryStage.show();
        primaryStage.setOnCloseRequest(we -> ((MainSceneController) loader.getController()).stop());
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
