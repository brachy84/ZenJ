package com.github.brachy84.zenj.parsing;

import com.github.brachy84.zenj.lang.ZenScript;
import com.github.brachy84.zenj.psi.ZsFile;
import com.github.brachy84.zenj.psi.ZsTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class ZsParserDefinition implements ParserDefinition {

    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(ZsTypes.LINE_COMMENT, ZsTypes.BLOCK_COMMENT);

    public static final IFileElementType FILE = new IFileElementType(ZenScript.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new ZsLexerAdapter();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new ZsParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return WHITE_SPACES;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return ZsTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new ZsFile(viewProvider);
    }

}
