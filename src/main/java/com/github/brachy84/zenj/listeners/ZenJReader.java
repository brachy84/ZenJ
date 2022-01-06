package com.github.brachy84.zenj.listeners;

import com.github.brachy84.zenj.lang.ZsClass;
import com.github.brachy84.zenj.lang.ZsMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ZenJReader {

    public static final String FILES_PATH = "/ZenJ";
    private final List<ZsMethod> METHODS = new ArrayList<>();
    private final List<ZsMethod> GETTERS = new ArrayList<>();
    private final List<ZsMethod> SETTERS = new ArrayList<>();
    private String name;
    private ReadMode mode;

    private ZenJReader() {
        this.mode = ReadMode.START;
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

    private boolean isMode(ReadMode readMode) {
        return mode == readMode;
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
        line = line.replaceAll(" ", "");
        if (isMode(ReadMode.START)) {
            mode = ReadMode.AFTER_START;
            name = line;
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
        List<String> parts = Arrays.stream(line.split("\\|")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        readMethod(parts);
    }

    public void readMethod(List<String> parts) {
        String name = parts.get(0);
        boolean isStatic = false;
        if(name.startsWith("$")) {
            name = name.substring(1);
            isStatic = true;
        }

        List<String> params;
        if (parts.size() == 2)
            params = Collections.emptyList();
        else
            params = parts
                    .subList(2, parts.size())
                    .stream()
                    .map(String::trim)
                    .filter(part -> !part.isEmpty())
                    .collect(Collectors.toList());

        switch (mode) {
            case GETTERS:
                GETTERS.add(ZsMethod.getter(name, parts.get(1), isStatic));
                break;
            case SETTERS:
                SETTERS.add(ZsMethod.setter(name, params.get(0), isStatic));
                break;
            case METHODS:
                METHODS.add(new ZsMethod(name, params, parts.get(1), ZsMethod.Type.METHOD, isStatic));
        }
    }

    public enum ReadMode {
        START,
        AFTER_START,
        GETTERS,
        SETTERS,
        METHODS
    }
}
