package com.tamagotchi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tamagotchi.model.view.IGameObjectView;
import com.tamagotchi.model.view.PetView;
import lombok.*;

import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    private static final double maxScale = 1.3;

    private int satiety = 100;
    @Getter
    private double scale = 1;
    @Getter
    @Setter
    private long growTime = 0;
    @Getter
    @Setter
    private long timeGotHungry = 0;
    @Getter
    @Setter
    private long timeDied = 0;
    @JsonIgnore
    private PetView image;

    @With
    @JsonIgnore
    private Consumer<Integer> onSatietyChange;
    @With
    @JsonIgnore
    private Consumer<PetView> onCleanView;

    public Pet(Consumer<PetView> onCreateView,
               Consumer<PetView> onCleanView,
               Consumer<Integer> onSatietyChange,
               Consumer<IGameObjectView> onIntersect) {
        this.image = new PetView(onIntersect);
        this.onCleanView = onCleanView;
        this.onSatietyChange = onSatietyChange;
        onCreateView.accept(image);
    }

    public void clean() {
        onCleanView.accept(image);
    }

    public Pet withImage(Consumer<PetView> onCreateView, Consumer<IGameObjectView> onIntersect) {
        PetView image = new PetView(onIntersect);
        Pet pet = new Pet(this.satiety, this.scale, this.growTime, this.timeGotHungry, this.timeDied, image,
                this.onSatietyChange, this.onCleanView);
        onCreateView.accept(image);
        pet.setScale(this.scale);
        return pet;
    }

    public int getSatiety() {
        return Math.min(Math.max(this.satiety, 0), 100);
    }

    public void setSatiety(int satiety) {
        this.satiety = Math.min(Math.max(satiety, 0), 100);
        if (onSatietyChange != null)
            onSatietyChange.accept(this.satiety);
    }

    public void setScale(double scale) {
        if (scale >= 1 && scale <= maxScale)
            this.scale = scale;
        if (this.image != null) {
            this.image.setScaleX(scale);
            this.image.setScaleY(scale);
        }
    }

    public void movePet(int value) {
        this.image.setX(this.image.getX() + value);
    }

    public boolean growUp() {
        if (getScale() < maxScale) {
            setScale(getScale() + 0.02);
            return true;
        } else
            return false;
    }
}
