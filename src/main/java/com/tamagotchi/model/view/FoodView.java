package com.tamagotchi.model.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.util.Random;

public class FoodView extends ImageView {
    public FoodView() {
        super();
        InputStream iconStream = getClass().getResourceAsStream("/images/food1.png");
        Image image = new Image(iconStream);
        this.setImage(image);
        this.setFitHeight(56);
        this.setFitWidth(56);
        this.setY(100);
        if (new Random().nextInt(2) == 0)
            this.setX(new Random().nextInt(320));
        else
            this.setX(new Random().nextInt(320) + 320);
    }
}
