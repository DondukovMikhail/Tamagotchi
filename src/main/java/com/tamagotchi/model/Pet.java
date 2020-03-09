package com.tamagotchi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tamagotchi.model.view.IGameObjectView;
import com.tamagotchi.model.view.PetView;
import lombok.*;
import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    private static final double maxScale = 1.21;
    private static final double deltaScale = 0.02;
    private static final int minSatiety = 0;
    private static final int maxSatiety = 100;

    public static final int foodSatietyDelta = 10;
    public static final double satietyChangeTime = 108000;
    public static final double growthTime = 1800000;
    public static final double badMoodTimeToDeath = 10800000;

    @Getter
    @Setter
    private int imageType = 1;
    @Getter
    private int satiety = 100;
    @Getter
    private double scale = 1;
    @Getter
    @Setter
    private long growTime = 0;
    @Getter
    private long timeGotHungry = 0;
    @Getter
    private long timeSatietyChanged = 0;
    @Getter
    @JsonIgnore
    private PetView image;

    @With
    @JsonIgnore
    private Consumer<Integer> onSatietyChange;
    @With
    @JsonIgnore
    private Consumer<PetView> onCleanView;
    @With
    @JsonIgnore
    private Consumer<Long> onTimeGotHungryChange;

    public Pet(int imageType,
               Consumer<PetView> onCreateView,
               Consumer<PetView> onCleanView,
               Consumer<Integer> onSatietyChange,
               Consumer<Long> onTimeGotHungryChange,
               Consumer<IGameObjectView> onIntersect) {
        this.imageType = imageType;
        this.image = new PetView(imageType, onIntersect);
        this.onCleanView = onCleanView;
        this.onSatietyChange = onSatietyChange;
        this.onTimeGotHungryChange = onTimeGotHungryChange;
        onCreateView.accept(image);
        setTimeGotHungry(this.timeGotHungry);
        setSatiety(this.satiety);
        calcGrowthAndSatietyChangeWhileClosed();
    }

    public void clean() {
        onCleanView.accept(image);
    }

    public Pet withImage(Consumer<PetView> onCreateView, Consumer<IGameObjectView> onIntersect) {
        PetView image = new PetView(this.imageType, onIntersect);
        Pet pet = new Pet(this.imageType, this.satiety, this.scale, this.growTime, this.timeGotHungry,
                this.timeSatietyChanged, image, this.onSatietyChange, this.onCleanView, this.onTimeGotHungryChange);
        onCreateView.accept(image);
        pet.setScale(this.scale);
        pet.setSatiety(this.satiety);
        pet.setTimeGotHungry(this.timeGotHungry);
        pet.calcGrowthAndSatietyChangeWhileClosed();
        return pet;
    }

    public void setSatiety(int satiety) {
        this.satiety = Math.min(Math.max(satiety, minSatiety), maxSatiety);
        this.timeSatietyChanged = System.currentTimeMillis();
        if (onSatietyChange != null)
            onSatietyChange.accept(this.satiety);
    }

    public void setTimeGotHungry(long timeGotHungry) {
        this.timeGotHungry = timeGotHungry;
        if (onTimeGotHungryChange != null)
            onTimeGotHungryChange.accept(this.timeGotHungry);
    }

    public void setScale(double scale) {
        if (scale >= 1 && scale <= maxScale) {
            this.scale = scale;
            if (this.image != null) {
                this.image.setScaleX(scale);
                this.image.setScaleY(scale);
            }
        }
    }

    public void movePet(int value) {
        this.image.setX(this.image.getX() + value);
    }

    public boolean growUp() {
        if (this.scale < maxScale) {
            this.setGrowTime(System.currentTimeMillis());
            setScale(this.scale + deltaScale);
            return true;
        } else
            return false;
    }

    private void calcGrowthAndSatietyChangeWhileClosed() {
        if (timeSatietyChanged == 0 || satiety == 0)
            return;

        long currentTime = System.currentTimeMillis();
        long SatietyDeltaTime = currentTime - timeSatietyChanged;
        System.out.println("delta: " + SatietyDeltaTime);
        int newSatiety = this.satiety - (int)(SatietyDeltaTime / satietyChangeTime);
        System.out.println("old: " + this.satiety + ", new: " + newSatiety);
        int timesSatietyChanged;
        if (newSatiety <= 0)
            timesSatietyChanged  = this.satiety;
        else
            timesSatietyChanged = this.satiety - newSatiety;
        double timeStayedHappy = timesSatietyChanged * satietyChangeTime;
        long timeGotUnhappy = currentTime;
        if (newSatiety <= 0)
            timeGotUnhappy = (int)(timesSatietyChanged + timesSatietyChanged * satietyChangeTime);

        setScale(Math.min(this.scale + deltaScale * timeStayedHappy / growthTime, maxScale));
        setSatiety(newSatiety);

        if (this.satiety == 0 && this.timeGotHungry == 0) {
            setTimeGotHungry(timeGotUnhappy);
        }
    }
}
