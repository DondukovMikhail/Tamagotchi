package com.tamagotchi.dao;

import com.tamagotchi.exception.GameFileNotFoundException;

import java.io.IOException;

public interface IFileSystemDAO {
    String read(String file) throws GameFileNotFoundException, IOException;
    void write(String file, String data) throws IOException;
}
