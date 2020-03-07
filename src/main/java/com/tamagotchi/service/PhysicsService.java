package com.tamagotchi.service;

import com.tamagotchi.model.view.IGameObjectView;

import java.util.LinkedList;
import java.util.List;

public class PhysicsService {
    private List<IGameObjectView> objectsToProcess;

    public PhysicsService() {
        objectsToProcess = new LinkedList<>();
    }

    public void registerView(IGameObjectView obj) {
        objectsToProcess.add(obj);
    }

    public void removeView(IGameObjectView obj) {
        objectsToProcess.remove(obj);
    }

    public void process(long delta) {
        for (IGameObjectView obj : objectsToProcess) {
            for (IGameObjectView checkObj : objectsToProcess) {
                if (obj != checkObj && obj.intersects(checkObj.getBoundsLocal())) {
                    obj.onIntersect(checkObj);
                }
            }
        }
    }
}
