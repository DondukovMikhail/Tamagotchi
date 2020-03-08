package com.tamagotchi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamagotchi.dao.FileSystemDAO;
import com.tamagotchi.exception.GameFileNotFoundException;
import com.tamagotchi.model.Pet;
import com.tamagotchi.model.view.FoodView;
import com.tamagotchi.model.view.IGameObjectView;
import com.tamagotchi.model.view.PetView;

import java.io.IOException;
import java.util.function.Consumer;

public class PetLifecycleService implements IGameService {
    private Pet pet;
    private double hungerTime;
    private double growTime;

    private ObjectMapper objectMapper;
    private FileSystemDAO fileSystemDAO;

    public PetLifecycleService() {
        hungerTime = 0;
        objectMapper = new ObjectMapper();
        fileSystemDAO = FileSystemDAO.getInstance();
    }

    public void createPet(Consumer<PetView> onCreateView, Consumer<PetView> onCleanView,
                          Consumer<Integer> onSatietyChange) {
        if (pet == null) {
            try {
                pet = deserializePet(onCreateView, onCleanView, onSatietyChange);
            } catch (GameFileNotFoundException e) {
                pet = new Pet(onCreateView, onCleanView, onSatietyChange, this::processIntersect);
            }
            growTime = pet.getGrowTime();
        }
    }

    private void deletePet() {
        if (pet != null) {
            try {
                serializePet();
            } catch (IOException e) {
                // Handle e
            }
            pet.clean();
            pet = null;
        }
    }

    private void serializePet() throws JsonProcessingException {
        String jsonStr = objectMapper.writeValueAsString(pet);
        try {
            fileSystemDAO.write("pet.json", jsonStr);
        } catch (IOException e) {
            // Handle e
        }
    }

    private Pet deserializePet(Consumer<PetView> onCreateView, Consumer<PetView> onCleanView,
                               Consumer<Integer> onSatietyChange) throws GameFileNotFoundException {
        try {
            Pet pet = objectMapper.readValue(fileSystemDAO.read("pet.json"), Pet.class);
            return pet
                    .withOnCleanView(onCleanView)
                    .withOnSatietyChange(onSatietyChange)
                    .withImage(onCreateView, this::processIntersect);
        } catch (IOException e) {
            // Handle e
            throw new RuntimeException(e);
        }
    }

    @Override
    public void processIntersect(IGameObjectView other) {
        if (other instanceof FoodView) {
            pet.setSatiety(pet.getSatiety() + 10);
        }
    }

    @Override
    public void stop() {
        deletePet();
    }

    public void movePet(int value) {
        pet.movePet(value);
    }

    private void growPet() {
        if (pet.growUp()) growTime = 0;
    }

    public void update(long delta) {
        if (pet == null)
            return;

        hungerTime += delta;
        growTime += delta;
        if (hungerTime > 250) {
            if (pet.getSatiety() > 0) {
                pet.setSatiety(pet.getSatiety() - 1);
                if (growTime > 1000) {
                    growPet();
                }
            } else {
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
