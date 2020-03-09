package com.tamagotchi.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class PetChooseSceneController {

    private MainSceneController mainSceneController;

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
        mainSceneController = loader.getController();
        stage.setOnCloseRequest(we -> mainSceneController.stop());
        stage.show();
    }

    @FXML
    private void pet1ButtonClicked() throws IOException {
        nextScene();
        mainSceneController.initPet(1);
    }

    @FXML
    private void pet2ButtonClicked() throws IOException {
        nextScene();
        mainSceneController.initPet(2);
    }

    @FXML
    private void pet3ButtonClicked() throws IOException {
        nextScene();
        mainSceneController.initPet(3);
    }
}
