package com.github.brachy84.zenj.listeners;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ZsMethod {

    public static final String VOID = "VOID";

    private final String name;
    private final List<String> parameters;
    private final String returns;
    private final Type type;

    public ZsMethod(String name, List<String> parameters, String returns, Type type) {
        this.name = name;
        this.parameters = parameters;
        this.returns = returns;
        this.type = type;
    }

    public static ZsMethod getter(String name, String returns) {
        return new ZsMethod(name, new ArrayList<>(), returns, Type.GETTER);
    }

    public static ZsMethod setter(String name, String parameter) {
        return new ZsMethod(name, Lists.newArrayList(parameter), VOID, Type.SETTER);
    }

    public boolean hasParameters() {
        return parameters.size() > 0;
    }

    public boolean returnsVoid() {
        return returns.equals(VOID);
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public String getReturns() {
        return returns;
    }

    public Type getType() {
        return type;
    }

    public boolean isType(Type type) {
        return this.type == type;
    }

    public enum Type {
        METHOD,
        GETTER,
        SETTER
    }
}
