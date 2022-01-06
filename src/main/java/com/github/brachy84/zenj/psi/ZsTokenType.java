package com.github.brachy84.zenj.psi;

import com.github.brachy84.zenj.lang.ZenScript;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class ZsTokenType extends IElementType {

    public ZsTokenType(@NotNull String debugName) {
        super(debugName, ZenScript.INSTANCE);
    }

    @Override
    public String toString() {
        return "ZsTokenType." + super.toString();
    }
}
