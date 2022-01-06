package com.github.brachy84.zenj.psi;

import com.github.brachy84.zenj.lang.ZsFileType;
import com.github.brachy84.zenj.lang.ZenScript;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class ZsFile extends PsiFileBase {

    public ZsFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ZenScript.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return ZsFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "ZenScript File";
    }
}
