package com.tamagotchi.model.view;

import java.util.Random;
import java.util.function.Consumer;

public class FoodView extends AbstractGameObjectView {
    public FoodView(int imageType, Consumer<IGameObjectView> onIntersect) {
        super(onIntersect, "/images/food" + imageType + ".png");
        this.setFitHeight(56);
        this.setFitWidth(56);
        this.setY(250);
        if (new Random().nextInt(2) == 0)
            this.setX(new Random().nextInt(320));
        else
            this.setX(new Random().nextInt(320) + 270);
    }
}
