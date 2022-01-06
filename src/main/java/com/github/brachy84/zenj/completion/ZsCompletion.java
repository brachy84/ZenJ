package com.github.brachy84.zenj.completion;

import com.github.brachy84.zenj.listeners.ZsClass;
import com.github.brachy84.zenj.psi.ZsFile;
import com.github.brachy84.zenj.psi.ZsTypes;
import com.github.brachy84.zenj.psi.ZsUtil;
import com.github.brachy84.zenj.psi.ZsVariable;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZsCompletion extends CompletionContributor {

    private static final String[] keywords = {
            "any",
            "bool",
            "byte",
            "short",
            "int",
            "long",
            "float",
            "double",
            "string",
            "function",
            "in",
            "void",
            "as",
            "version",
            "if",
            "else",
            "for",
            "return",
            "var",
            "val",
            "null",
            "true",
            "false",
            "import",
    };

    public ZsCompletion() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(ZsTypes.IDENTIFIER), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                for (String keyword : keywords) {
                    result.addElement(LookupElementBuilder.create(keyword));
                }

                if (parameters.getOriginalFile() instanceof ZsFile){

                    List<ZsVariable> properties = ZsUtil.findVariables((ZsFile) parameters.getOriginalFile());
                    Set<String> alreadyAdded = new HashSet<>();

                    for (final ZsVariable property : properties) {
                        if (property.getName() != null && property.getName().length() > 0) {
                            if (!alreadyAdded.contains(property.getName())){
                                alreadyAdded.add(property.getName());

                                result.addElement(LookupElementBuilder.create(property));
                            }
                        }
                    }
                }
            }
        });
        extend(CompletionType.CLASS_NAME, PlatformPatterns.psiElement(ZsTypes.IDENTIFIER), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                for(ZsClass zsClass: ZsClass.getAll()) {
                    result.addElement(LookupElementBuilder.create(zsClass));
                }
            }
        });
    }
}
