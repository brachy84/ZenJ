package com.github.brachy84.zenj.listeners;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ZsClass {

    private static final Map<String, ZsClass> ZS_CLASSES = new HashMap<>();

    @Nullable
    public static ZsClass getByName(String name) {
        return ZS_CLASSES.get(name);
    }

    public static Collection<ZsClass> getAll() {
        return ZS_CLASSES.values();
    }

    public static Set<String> getAllClassNames() {
        return ZS_CLASSES.keySet();
    }

    private final String name;
    private final String canonicalName;
    private final Map<String, ZsMethod> methods;
    private final Map<String, ZsMethod> getters;
    private final Map<String, ZsMethod> setters;

    private ZsClass(String name, List<ZsMethod> methods, List<ZsMethod> getters, List<ZsMethod> setters) {
        this.name = name;
        this.methods = methods.stream().collect(Collectors.toMap(ZsMethod::getName, method -> method));
        this.getters = methods.stream().collect(Collectors.toMap(ZsMethod::getName, method -> method));
        this.setters = methods.stream().collect(Collectors.toMap(ZsMethod::getName, method -> method));
        if (!name.contains("."))
            this.canonicalName = name;
        else {
            String[] parts = name.split("\\.");
            this.canonicalName = parts[parts.length - 1];
        }
    }

    public static ZsClass register(String name, List<ZsMethod> methods, List<ZsMethod> getters, List<ZsMethod> setters) {
        ZsClass zsClass = new ZsClass(name, methods, getters, setters);
        ZS_CLASSES.put(name, zsClass);
        return zsClass;
    }

    public String getName() {
        return name;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public Collection<ZsMethod> getMethods() {
        return methods.values();
    }

    public Collection<ZsMethod> getGetters() {
        return getters.values();
    }

    public Collection<ZsMethod> getSetters() {
        return setters.values();
    }

    @Nullable
    public ZsMethod getGetter(String name) {
        return getters.get(name);
    }

    @Nullable
    public ZsMethod getSetter(String name) {
        return setters.get(name);
    }

    @Nullable
    public ZsMethod getMethod(String name) {
        return methods.get(name);
    }

    @Nullable
    public ZsMethod getAny(String name) {
        ZsMethod method = getGetter(name);
        if(method != null) return method;
        method = getSetter(name);
        if(method != null) return method;
        return getMethod(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
