package com.tamagotchi.model.view;

import java.util.function.Consumer;

public class PetView extends AbstractGameObjectView {
    public PetView(Consumer<IGameObjectView> onIntersect) {
        super(onIntersect, "/images/pet1.png");
        this.setFitHeight(240);
        this.setFitWidth(240);
        this.setX(100);
        this.setY(100);
    }
}
