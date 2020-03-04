package com.tamagotchi.model.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class PetView extends ImageView {
    public PetView() {
        super();
        InputStream iconStream = getClass().getResourceAsStream("/images/pet1.png");
        Image image = new Image(iconStream);
        this.setImage(image);
        this.setFitHeight(240);
        this.setFitWidth(240);
        this.setX(100);
        this.setY(100);
    }
}
