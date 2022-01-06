package com.github.brachy84.zenj.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ZsFileType extends LanguageFileType {

    public static final ZsFileType INSTANCE = new ZsFileType();

    private ZsFileType() {
        super(ZenScript.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "ZenScript";
    }

    @Override
    public @NotNull @NlsContexts.Label String getDescription() {
        return "ZenScript file";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "zs";
    }

    @Override
    public @Nullable Icon getIcon() {
        return ZenScript.ICON;
    }
}
