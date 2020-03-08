package com.tamagotchi.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class PetChooseSceneController {

    @FXML
    private Pane petChoosePane;

    public PetChooseSceneController() {
    }

    @FXML
    private void nextScene() throws IOException {
        Stage stage = (Stage) petChoosePane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(we -> ((MainSceneController) loader.getController()).stop());
    }
}
