package com.tamagotchi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamagotchi.dao.FileSystemDAO;
import com.tamagotchi.exception.GameFileNotFoundException;
import com.tamagotchi.model.GameObject;

import java.io.IOException;

public class GameObjectLifecycleService {
    private GameObject gameObject;

    private ObjectMapper objectMapper;
    private FileSystemDAO fileSystemDAO;

    public GameObjectLifecycleService() {
        objectMapper = new ObjectMapper();
        fileSystemDAO = FileSystemDAO.getInstance();

        createGameObject();
    }

    public void createGameObject() {
        if (gameObject == null)
            try {
                gameObject = deserializeGameObject();
            } catch (GameFileNotFoundException e) {
                gameObject = new GameObject();
            }
    }

    private void deleteGameObject() {
        if (gameObject != null) {
            try {
                serializeGameObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            gameObject = null;
        }
    }

    public void stop() {
        deleteGameObject();
    }

    private GameObject deserializeGameObject() throws GameFileNotFoundException {
        try {
            return objectMapper.readValue(fileSystemDAO.read("game.json"), GameObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void serializeGameObject() throws JsonProcessingException {
        String jsonStr = objectMapper.writeValueAsString(gameObject);
        try {
            fileSystemDAO.write("game.json", jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean petIsDead() {
        return gameObject.getTimePetDied() != 0;
    }

    public long getTimePetDied() {
        return gameObject.getTimePetDied();
    }

    public void setTimePetDied(long timePetDied) {
        gameObject.setTimePetDied(timePetDied);
    }
}
