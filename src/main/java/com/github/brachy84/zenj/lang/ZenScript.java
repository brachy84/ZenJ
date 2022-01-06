package com.github.brachy84.zenj.lang;

import com.intellij.lang.Language;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ZenScript extends Language {

    public static final ZenScript INSTANCE = new ZenScript();

    public static final Icon ICON = IconLoader.getIcon("");

    private ZenScript() {
        super("ZenScript");
    }
}
