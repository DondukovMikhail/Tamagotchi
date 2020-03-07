package com.tamagotchi.model.view;

import javafx.geometry.Bounds;

public interface IGameObjectView {
    void onIntersect(IGameObjectView other);
    boolean intersects(Bounds localBounds);
    Bounds getBoundsLocal();
}
