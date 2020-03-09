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
    private double hungerTimer;
    private double growTimer;
    private int chosenType = 1;

    private ObjectMapper objectMapper;
    private FileSystemDAO fileSystemDAO;

    public Consumer<Long> onPetDied;
    public Consumer<Integer> onChosenTypeChanged;

    public PetLifecycleService(Consumer<Integer> onChosenTypeChanged) {
        hungerTimer = 0;
        objectMapper = new ObjectMapper();
        fileSystemDAO = FileSystemDAO.getInstance();
        this.onChosenTypeChanged = onChosenTypeChanged;
    }

    public void createPet(Consumer<PetView> onCreateView, Consumer<PetView> onCleanView,
                          Consumer<Integer> onSatietyChange, Consumer<Long> onTimeGotHungryChange) {
        if (pet == null) {
            try {
                pet = deserializePet(onCreateView, onCleanView, onSatietyChange, onTimeGotHungryChange);
                setChosenType(pet.getImageType());
            } catch (GameFileNotFoundException e) {
                pet = new Pet(chosenType, onCreateView, onCleanView, onSatietyChange, onTimeGotHungryChange, this::processIntersect);
            }
            growTimer = pet.getGrowTime();
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
            throw new RuntimeException(e);
        }
    }

    private Pet deserializePet(Consumer<PetView> onCreateView, Consumer<PetView> onCleanView,
                               Consumer<Integer> onSatietyChange, Consumer<Long> onTimeGotHungryChange) throws GameFileNotFoundException {
        try {
            Pet pet = objectMapper.readValue(fileSystemDAO.read("pet.json"), Pet.class);
            return pet
                    .withOnCleanView(onCleanView)
                    .withOnSatietyChange(onSatietyChange)
                    .withOnTimeGotHungryChange(onTimeGotHungryChange)
                    .withImage(onCreateView, this::processIntersect);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void processIntersect(IGameObjectView other) {
        if (other instanceof FoodView) {
            pet.setSatiety(pet.getSatiety() + Pet.foodSatietyDelta);
            pet.setTimeGotHungry(0);
        }
    }

    @Override
    public void stop() {
        deletePet();
    }

    public void movePet(int value) {
        if (pet == null || pet.getImage() == null)
            return;

        if ((value < 0 && pet.getImage().getX() - value > 0) || (value > 0 && pet.getImage().getX() + pet.getImage().getFitWidth() + value < 680))
            pet.movePet(value);
    }

    private void growPet() {
        if (pet.growUp()) growTimer = 0;
    }

    public void update(long delta) {
        if (pet == null)
            return;

        hungerTimer += delta;
        if (pet.getSatiety() > 0)
            growTimer += delta;
        long timeNow = System.currentTimeMillis();

        if (hungerTimer > Pet.satietyChangeTime) {
            if (pet.getSatiety() > 0) {
                pet.setSatiety(pet.getSatiety() - 1);
                if (growTimer > Pet.growthTime) growPet();
                if (pet.getSatiety() == 0 && pet.getTimeGotHungry() == 0) {
                    pet.setTimeGotHungry(timeNow);
                    growTimer = 0;
                }
            } else if (pet.getTimeGotHungry() != 0 && timeNow - pet.getTimeGotHungry() > Pet.badMoodTimeToDeath) {
                onPetDied.accept(timeNow);
                deletePet();
            }

            hungerTimer = 0;
        }
    }

    public void setChosenType(int chosenType) {
        this.chosenType = chosenType;
        if (onChosenTypeChanged != null)
            onChosenTypeChanged.accept(this.chosenType);
    }
}
