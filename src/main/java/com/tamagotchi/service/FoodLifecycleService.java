package com.tamagotchi.service;

import com.tamagotchi.model.Food;
import com.tamagotchi.model.view.FoodView;

import java.util.function.Consumer;

public class FoodLifecycleService {
    private Food food;

    public FoodLifecycleService() {
    }

    public void createFood(Consumer<FoodView> onCreateView, Consumer<FoodView> onCleanView) {
        if (food == null) {
            food = new Food(onCreateView, onCleanView);
        }
    }

    public void deleteFood() {
        if (food != null) {
            food.clean();
            food = null;
        }
    }
}
