package com.github.brachy84.zenj.parsing;

import com.github.brachy84.zenj.psi._ZsLexer;
import com.intellij.lexer.FlexAdapter;

public class ZsLexerAdapter extends FlexAdapter {
    public ZsLexerAdapter() {
        super(new _ZsLexer(null));
    }
}
