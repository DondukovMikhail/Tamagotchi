package com.tamagotchi.service;

import com.tamagotchi.model.view.IGameObjectView;

public interface IGameService {
    void processIntersect(IGameObjectView other);
    void stop();
}
