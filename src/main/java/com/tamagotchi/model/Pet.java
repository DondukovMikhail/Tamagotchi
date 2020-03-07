package com.tamagotchi.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamagotchi.model.view.IGameObjectView;
import com.tamagotchi.model.view.PetView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class Pet {
    private long growTime;
    private double scale;
    private long timeGotHungry;
    private long timeDied;
    private int satiety;
    private Consumer<Integer> onSatietyChange;

    private PetView image;
    private Consumer<PetView> onCleanView;

    public Pet(Consumer<PetView> onCreateView,
               Consumer<PetView> onCleanView,
               Consumer<Integer> onSatietyChange,
               Consumer<IGameObjectView> onIntersect) throws IOException {
        this.image = new PetView(onIntersect);
        this.onCleanView = onCleanView;
        this.onSatietyChange = onSatietyChange;
        setSatiety(100);
        setTimeGotHungry(0);
        setTimeDied(0);
        setGrowTime(0);
        scale = 1;
        deserialize();
        onCreateView.accept(image);
    }

    public long getGrowTime() {
        return growTime;
    }

    public void setGrowTime(long growTime) {
        this.growTime = growTime;
    }

    public long getTimeGotHungry() {
        return timeGotHungry;
    }

    public void setTimeGotHungry(long timeGotHungry) {
        this.timeGotHungry = timeGotHungry;
    }

    public long getTimeDied() {
        return timeDied;
    }

    public void setTimeDied(long timeDied) {
        this.timeDied = timeDied;
    }

    public int getSatiety() {
        return Math.min(Math.max(this.satiety, 0), 100);
    }

    public void setSatiety(int satiety) {
        this.satiety = Math.min(Math.max(satiety, 0), 100);
        onSatietyChange.accept(this.satiety);
    }

    public void clean() throws IOException {
        serialize();
        onCleanView.accept(image);
    }

    private void serialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(this);
        BufferedWriter writer = new BufferedWriter(new FileWriter("pet.json"));
        writer.write(jsonString);
        writer.close();
    }

    private void deserialize() throws IOException {
        File jsonFile = new File("./pet.json");
        if (jsonFile.exists()) {
            String jsonString = new String(Files.readAllBytes(Paths.get("./pet.json")));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(jsonString);
            if (actualObj.has("satiety"))
                this.setSatiety(actualObj.get("satiety").asInt(0));
            if (actualObj.has("timeGotHungry"))
                this.setTimeGotHungry(actualObj.get("timeGotHungry").asLong(0));
            if (actualObj.has("timeDied"))
                this.setTimeGotHungry(actualObj.get("timeDied").asLong(0));
            if (actualObj.has("scale"))
                this.scale = actualObj.get("scale").asDouble(0);
        }
    }

    public void movePet(int value) {
        image.setX(image.getX() + value);
    }

    public void growUp() {
        if (scale < 1.3) {
            image.setScaleX(scale + 0.02);
            image.setScaleY(scale + 0.02);
            scale += 0.02;
        }
    }
}
