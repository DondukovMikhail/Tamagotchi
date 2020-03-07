package com.tamagotchi.model;

import com.tamagotchi.model.view.FoodView;
import com.tamagotchi.model.view.IGameObjectView;

import java.util.function.Consumer;

public class Food {
    private FoodView image;
    private Consumer<FoodView> onCleanView;

    public Food(Consumer<FoodView> onCreateView, Consumer<FoodView> onCleanView, Consumer<IGameObjectView> onIntersect) {
        this.image = new FoodView(onIntersect);
        this.onCleanView = onCleanView;
        onCreateView.accept(image);
    }

    public void clean() {
        onCleanView.accept(image);
    }
}
