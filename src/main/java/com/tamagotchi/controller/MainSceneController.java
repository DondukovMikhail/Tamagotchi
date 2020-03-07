package com.tamagotchi.controller;

import com.tamagotchi.model.Food;
import com.tamagotchi.service.FoodLifecycleService;
import com.tamagotchi.service.PetLifecycleService;
import com.tamagotchi.service.PhysicsService;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainSceneController {

    private AnimationTimer timer;

    private long previousTime;

    private PetLifecycleService petLifecycleService;
    private FoodLifecycleService foodLifecycleService;
    private PhysicsService physicsService;

    @FXML
    private Label satietyLabel;

    @FXML
    private Pane mainPane;


    public MainSceneController() {
        previousTime = System.currentTimeMillis();
        petLifecycleService = new PetLifecycleService();
        foodLifecycleService = new FoodLifecycleService();
        physicsService = new PhysicsService();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long systemNow = System.currentTimeMillis();
                long delta = systemNow - previousTime;
                try {
                    petLifecycleService.update(delta);
                    physicsService.process(delta);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                previousTime = systemNow;
            }
        };
    }

    @FXML
    private void buttonClicked() throws IOException {
        petLifecycleService.createPet(
                it -> {
                    mainPane.getChildren().add(it);
                    physicsService.registerView(it);
                },
                it -> {
                    mainPane.getChildren().remove(it);
                    physicsService.removeView(it);
                },
                it -> satietyLabel.setText(it.toString())
        );
        timer.start();
    }

    @FXML
    private void buttonClicked1() throws IOException {
        timer.stop();
        petLifecycleService.deletePet();
    }

    @FXML
    private void buttonClicked2() {
        petLifecycleService.movePet(-5);
    }

    @FXML
    private void buttonClicked3() {
        petLifecycleService.movePet(5);
    }

    @FXML
    private void buttonClicked4() {
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

    @FXML
    private void buttonClicked5() {
        foodLifecycleService.deleteFood();
    }
}
