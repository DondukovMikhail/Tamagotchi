package com.tamagotchi.model.view;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.function.Consumer;

public abstract class AbstractGameObjectView extends ImageView implements IGameObjectView {
    private Consumer<IGameObjectView> onIntersect;

    public AbstractGameObjectView(Consumer<IGameObjectView> onIntersect, String imagePath) {
        this.onIntersect = onIntersect;
        InputStream iconStream = getClass().getResourceAsStream(imagePath);
        Image image = new Image(iconStream);
        this.setImage(image);
    }

    public void onIntersect(IGameObjectView other) {
        onIntersect.accept(other);
    }

    public boolean intersects(Bounds localBounds) {
        return super.intersects(localBounds);
    }

    public Bounds getBoundsLocal() {
       return super.getBoundsInLocal();
    }
}
