package com.tamagotchi.model;

import com.tamagotchi.model.view.FoodView;

import java.util.function.Consumer;

public class Food {
    private FoodView image;
    private Consumer<FoodView> onCleanView;

    public Food(Consumer<FoodView> onCreateView, Consumer<FoodView> onCleanView) {
        this.image = new FoodView();
        this.onCleanView = onCleanView;
        onCreateView.accept(image);
    }

    public void clean() {
        onCleanView.accept(image);
    }

    public FoodView getImage() {
        return image;
    }
}
