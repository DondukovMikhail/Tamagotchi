package com.tamagotchi.dao;

import com.tamagotchi.exception.GameFileNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSystemDAO implements IFileSystemDAO {

    private static final FileSystemDAO INSTANCE = new FileSystemDAO();

    private FileSystemDAO() {
    }

    public static FileSystemDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public String read(String fileName) throws GameFileNotFoundException, IOException {
        File file = new File(fileName);
        if (!file.exists())
            throw new GameFileNotFoundException(fileName + " not found.");
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    @Override
    public void write(String fileName, String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(data);
        }
    }
}
