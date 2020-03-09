package com.tamagotchi.controller;

import com.tamagotchi.service.FoodLifecycleService;
import com.tamagotchi.service.GameObjectLifecycleService;
import com.tamagotchi.service.PetLifecycleService;
import com.tamagotchi.service.PhysicsService;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainSceneController {

    private AnimationTimer timer;
    private long previousTime;
    private int chosenType = 1;

    private GameObjectLifecycleService gameObjectLifecycleService;
    private PetLifecycleService petLifecycleService;
    private FoodLifecycleService foodLifecycleService;
    private PhysicsService physicsService;

    @FXML
    private Label satietyLabel;

    @FXML
    private Label moodLabel;

    @FXML
    private Label petIsDeadLabel;

    @FXML
    private Button newPetButton;

    @FXML
    private Pane mainPane;

    public MainSceneController() {
        previousTime = System.currentTimeMillis();
        gameObjectLifecycleService = new GameObjectLifecycleService();
        petLifecycleService = new PetLifecycleService(t -> foodLifecycleService.setChosenType(t));
        foodLifecycleService = new FoodLifecycleService();
        physicsService = new PhysicsService();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long systemNow = System.currentTimeMillis();
                long delta = systemNow - previousTime;
                petLifecycleService.update(delta);
                physicsService.process(delta);
                previousTime = systemNow;

                if (gameObjectLifecycleService.petIsDead()) {
                    if (petIsDeadLabel.getOpacity() == 0)
                        changeLabelsOpacity();
                    int minutesLeftToNewPet = Math.max((int)(15 - (systemNow - gameObjectLifecycleService.getTimePetDied()) / 60000), 0);
                    if (minutesLeftToNewPet > 0)
                        petIsDeadLabel.setText("Pet is dead.\nYou can create new in " + minutesLeftToNewPet + "min.");
                    else {
                        petIsDeadLabel.setText("Pet is dead.\nYou can create new now.");
                        newPetButton.setDisable(false);
                        newPetButton.setOpacity(1);
                    }
                }
            }
        };

        petLifecycleService.onPetDied = t -> {
            gameObjectLifecycleService.setTimePetDied(t);
            changeLabelsOpacity();
        };
        timer.start();
    }

    public void stop() {
        timer.stop();
        petLifecycleService.stop();
        foodLifecycleService.stop();
        gameObjectLifecycleService.stop();
    }

    public void initPet(int chosenType) {
        if (!gameObjectLifecycleService.petIsDead()) {
            setChosenType(chosenType);
            petLifecycleService.createPet(
                    it -> {
                        mainPane.getChildren().add(it);
                        physicsService.registerView(it);
                    },
                    it -> {
                        mainPane.getChildren().remove(it);
                        physicsService.removeView(it);
                    },
                    it -> satietyLabel.setText("Satiety level: " + it.toString()),
                    it -> {
                        if (it == 0)
                            moodLabel.setText("Mood: happy");
                        else
                            moodLabel.setText("Mood: unhappy");
                    }
            );
        }
    }

    @FXML
    private void moveLeftButtonClicked() {
        petLifecycleService.movePet(-10);
    }

    @FXML
    private void moveRightButtonClicked() {
        petLifecycleService.movePet(10);
    }

    @FXML
    private void feedButtonClicked() {
        if (!gameObjectLifecycleService.petIsDead()) {
            foodLifecycleService.createFood(
                    it -> {
                        mainPane.getChildren().add(it);
                        physicsService.registerView(it);
                    },
                    it -> {
                        mainPane.getChildren().remove(it);
                        physicsService.removeView(it);
                    }
            );
        }
    }

    @FXML
    private void newPetButtonClicked() throws IOException {
        stop();

        File file = new File("pet.json");
        file.delete();
        file = new File("game.json");
        file.delete();

        Stage stage = (Stage) mainPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/petChooseScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private void changeLabelsOpacity() {
        satietyLabel.setOpacity(0);
        moodLabel.setOpacity(0);
        petIsDeadLabel.setOpacity(1);
    }

    public void setChosenType(int chosenType) {
        this.chosenType = chosenType;
        petLifecycleService.setChosenType(chosenType);
        foodLifecycleService.setChosenType(chosenType);
    }
}
