package com.github.brachy84.zenj.formatter;

import com.github.brachy84.zenj.psi.ZsTypes;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ZsBraceMatcher implements PairedBraceMatcher {

    @Override
    public BracePair @NotNull [] getPairs() {
        return new BracePair[]{
                new BracePair(ZsTypes.L_ROUND_BRACKET, ZsTypes.R_ROUND_BRACKET, false),
                new BracePair(ZsTypes.L_ANGLE_BRACKET, ZsTypes.R_ANGLE_BRACKET, false),
                new BracePair(ZsTypes.L_SQUARE_BRACKET, ZsTypes.R_SQUARE_BRACKET, false),
                new BracePair(ZsTypes.L_CURLY_BRACKET, ZsTypes.R_CURLY_BRACKET, false),
        };
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType iElementType, @Nullable IElementType iElementType1) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return 0;
    }
}
