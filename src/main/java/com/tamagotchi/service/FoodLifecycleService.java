package com.tamagotchi.service;

import com.tamagotchi.model.Food;
import com.tamagotchi.model.view.FoodView;
import com.tamagotchi.model.view.IGameObjectView;
import com.tamagotchi.model.view.PetView;

import java.util.function.Consumer;

public class FoodLifecycleService implements IGameService {
    private Food food;

    public FoodLifecycleService() {
    }

    public void createFood(Consumer<FoodView> onCreateView, Consumer<FoodView> onCleanView) {
        if (food == null) {
            food = new Food(onCreateView, onCleanView, this::processIntersect);
        }
    }

    private void deleteFood() {
        if (food != null) {
            food.clean();
            food = null;
        }
    }

    @Override
    public void processIntersect(IGameObjectView other) {
        if (other instanceof PetView) {
            deleteFood();
        }
    }

    @Override
    public void stop() {
        deleteFood();
    }
}
