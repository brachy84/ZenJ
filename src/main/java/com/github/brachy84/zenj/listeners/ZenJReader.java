package com.github.brachy84.zenj.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ZenJReader {

    public static final String FILES_PATH = "/ZenJ";

    private String name;
    private final List<ZsMethod> METHODS = new ArrayList<>();
    private final List<ZsMethod> GETTERS = new ArrayList<>();
    private final List<ZsMethod> SETTERS = new ArrayList<>();

    private ReadMode mode;

    private ZenJReader() {
        this.mode = ReadMode.START;
    }

    private boolean isMode(ReadMode readMode) {
        return mode == readMode;
    }

    public static void readCTFiles(String projectPath) {
        Path path = Paths.get(projectPath + FILES_PATH);
        try {
            List<Path> paths = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            paths.forEach(path1 -> {
                ZenJReader reader = new ZenJReader();
                reader.readFile(path1);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(Path path) {
        try {
            Files.readAllLines(path).forEach(this::readLine);
            ZsClass.register(name, METHODS, GETTERS, SETTERS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readLine(String line) {
        if (isMode(ReadMode.START)) {
            name = line.trim();
            return;
        }
        if (line.startsWith("-") || line.startsWith("  NAME") || line.trim().equals("")) return;
        if (line.startsWith("//")) {
            if (line.contains("METHOD")) {
                mode = ReadMode.METHODS;
                return;
            }
            if (line.contains("GETTER")) {
                mode = ReadMode.GETTERS;
                return;
            }
            if (line.contains("SETTER")) {
                mode = ReadMode.SETTERS;
                return;
            }
        }
        line = line.replaceAll(" ", "");
        readMethod(line.split("\\|"));
    }

    public void readMethod(String[] parts) {
        List<String> params = Arrays.asList(parts)
                .subList(2, parts.length)
                .stream()
                .filter(part -> !part.isEmpty())
                .collect(Collectors.toList());
        switch (mode) {
            case GETTERS:
                GETTERS.add(ZsMethod.getter(parts[0], parts[1]));
                break;
            case SETTERS:
                SETTERS.add(ZsMethod.setter(parts[0], params.get(0)));
                break;
            case METHODS:
                METHODS.add(new ZsMethod(parts[0], params, parts[1], ZsMethod.Type.METHOD));
        }
    }

    public enum ReadMode {
        START,
        GETTERS,
        SETTERS,
        METHODS
    }
}
