package com.tamagotchi;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/mainScene.fxml"));
        MainSceneController controller = loader.getController();
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(480);
        primaryStage.setResizable(false);
        //primaryStage.setWidth(1000);
        //primaryStage.setHeight(800);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setTitle("Tamagotchi");
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
