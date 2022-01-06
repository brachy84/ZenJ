package com.github.brachy84.zenj.formatter;

import com.github.brachy84.zenj.lang.ZenScript;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*;

import javax.swing.*;
import java.util.Map;

public class ZsColorSettings implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = {
            createDesc("Keywords", ZsSyntaxHighlighter.KEYWORD),
            createDesc("Brackethandlers", ZsSyntaxHighlighter.BRACKET_HANDLER),
            createDesc("Preprocessors", ZsSyntaxHighlighter.PREPROCESSOR),
            createDesc("Operators", ZsSyntaxHighlighter.OPERATOR),
            createDesc("Block comments", BLOCK_COMMENT),
            createDesc("Line comments", LINE_COMMENT),
            createDesc("Numbers", ZsSyntaxHighlighter.NUMBER),
            createDesc("Strings", STRING)
    };

    private static AttributesDescriptor createDesc(String name, TextAttributesKey key) {
        return new AttributesDescriptor(name, key);
    }

    @Override
    public @Nullable Icon getIcon() {
        return ZenScript.ICON;
    }

    @Override
    public @NotNull SyntaxHighlighter getHighlighter() {
        return new ZsSyntaxHighlighter();
    }

    @Override
    public @NotNull String getDemoText() {
        return "#priority 10\n" +
                "\n" +
                "import mods.gregtech.Bestmod;\n" +
                "// line comment\n" +
                "val sus as IItemStack = <amogus:impostor>;\n" +
                "static stuff as string[] = [\n" +
                "    \"this\",\n" +
                "    \"that\"\n" +
                "];\n" +
                "/*\n" +
                "Block comment\n" +
                "*/";
    }

    @Override
    public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @Override
    public @NotNull @NlsContexts.ConfigurableName String getDisplayName() {
        return "ZenScript";
    }
}
