package com.tamagotchi.service;

import com.tamagotchi.model.Pet;
import com.tamagotchi.model.view.PetView;

import java.io.IOException;
import java.util.function.Consumer;

public class PetLifecycleService {
    private Pet pet;
    private double hungerTime;
    private double growTime;

    public PetLifecycleService() {
        hungerTime = 0;
    }

    public void createPet(Consumer<PetView> onCreateView, Consumer<PetView> onCleanView, Consumer<Integer> onSatietyChange) throws IOException {
        if (pet == null) {
            pet = new Pet(onCreateView, onCleanView, onSatietyChange);
            growTime = pet.getGrowTime();
        }
    }

    public void deletePet() throws IOException {
        if (pet != null) {
            pet.clean();
            pet = null;
        }
    }

    public void movePet(int value) {
        pet.movePet(value);
    }

    public void update(long delta) throws IOException {
        if (pet == null)
            return;

        hungerTime += delta;
        growTime += delta;
        if (hungerTime > 250) {
            if (pet.getSatiety() > 0) {
                pet.setSatiety(pet.getSatiety() - 1);
                if (growTime > 1000) {
                    pet.growUp();
                    growTime = 0;
                }
            }
            else {
                long timeNow = System.currentTimeMillis();
                growTime = 0;
                if (pet.getTimeGotHungry() == 0) {
                    pet.setTimeGotHungry(timeNow);
                } else if (timeNow - pet.getTimeGotHungry() > 2000) {
                    pet.setTimeDied(timeNow);
                    deletePet();
                }
            }
            hungerTime = 0;
        }
    }
}
