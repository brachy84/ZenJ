package com.github.brachy84.zenj.formatter;

import com.github.brachy84.zenj.parsing.ZsLexerAdapter;
import com.github.brachy84.zenj.psi.ZsTypes;
import com.intellij.lexer.Lexer;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ZsSyntaxHighlighter extends SyntaxHighlighterBase {


    private static final Map<IElementType, TextAttributesKey[]> KEYS = new HashMap<>();

    private static void putSingle(IElementType type, TextAttributesKey key) {
        KEYS.put(type, new TextAttributesKey[]{key});
    }

    private static final IElementType[] KEYWORDS = {
            ZsTypes.IMPORT,
            ZsTypes.IF,
            ZsTypes.FOR,
            ZsTypes.TRUE,
            ZsTypes.FALSE,
            ZsTypes.FLOAT,
            ZsTypes.BOOL,
            ZsTypes.INT,
            ZsTypes.DOUBLE,
            ZsTypes.VAL,
            ZsTypes.VAR,
            ZsTypes.AS,
            ZsTypes.TO,
            ZsTypes.STATIC,
            ZsTypes.GLOBAL
    };

    private static final IElementType[] OPERATORS = {
            ZsTypes.AND,
            ZsTypes.OR,
            ZsTypes.PLUS,
            ZsTypes.MINUS,
            ZsTypes.PERC,
            ZsTypes.EQUAL,
            ZsTypes.EQEQ,
            ZsTypes.NOT_EQUAL,
            ZsTypes.TILDE,
            ZsTypes.XOR,
            ZsTypes.QUEST,
            ZsTypes.LESS_EQUAL,
            ZsTypes.GREATER_EQUAL,
            ZsTypes.DOTDOT,
            ZsTypes.ASTERISK,
            ZsTypes.DIV
    };

    public static final TextAttributesKey BRACKET_HANDLER = createKey("ZS_BRACKET_HANDLER");
    public static final TextAttributesKey PREPROCESSOR = createKey("ZS_PREPROCESSOR");
    public static final TextAttributesKey KEYWORD = createKey("ZS_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey NUMBER = createKey("ZS_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey OPERATOR = createKey("ZS_OPERATOR", OPERATION_SIGN);

    static {
        putSingle(ZsTypes.BRACKET_HANDLER, BRACKET_HANDLER);
        putSingle(ZsTypes.BLOCK_COMMENT, BLOCK_COMMENT);
        putSingle(ZsTypes.LINE_COMMENT, LINE_COMMENT);
        putSingle(ZsTypes.NUMBER, NUMBER);
        for(IElementType type : KEYWORDS) {
            putSingle(type, KEYWORD);
        }
        for(IElementType type : OPERATORS) {
            putSingle(type, OPERATION_SIGN);
        }
        putSingle(ZsTypes.DOUBLE_QUOTED_STRING, STRING);
        putSingle(ZsTypes.COMMA, COMMA);
        putSingle(ZsTypes.SEMICOLON, SEMICOLON);
        putSingle(ZsTypes.PREPROCESSOR_STATEMENT, PREPROCESSOR);
    }


    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new ZsLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        TextAttributesKey[] keys = KEYS.get(tokenType);
        if(keys != null)
            return keys;
        else
            return new TextAttributesKey[0];
    }

    private static TextAttributesKey createKey(String name) {
        return TextAttributesKey.createTextAttributesKey(name);
    }

    private static TextAttributesKey createKey(String name, TextAttributesKey key) {
        return TextAttributesKey.createTextAttributesKey(name, key);
    }

}
