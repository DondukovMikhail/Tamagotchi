package com.tamagotchi.model;

import lombok.Getter;
import lombok.Setter;

public class GameObject {
    @Getter
    @Setter
    private long timePetDied = 0;

    public GameObject() {
    }
}
