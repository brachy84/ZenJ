package com.github.brachy84.zenj.psi;

import com.github.brachy84.zenj.lang.ZenScript;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class ZsElementType extends IElementType {

    public ZsElementType(@NotNull String debugName) {
        super(debugName, ZenScript.INSTANCE);
    }

    @Override
    public String toString() {
        return "ZsElementType." + super.toString();
    }
}
