package com.github.brachy84.zenj.completion;

import com.github.brachy84.zenj.lang.ZenScript;
import com.github.brachy84.zenj.lang.ZsClass;
import com.github.brachy84.zenj.lang.ZsMethod;
import com.github.brachy84.zenj.psi.*;
import com.github.brachy84.zenj.psi.impl.ZsImportListImpl;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ZsCompletion extends CompletionContributor {

    public static final Map<String, String> GLOBALS = new HashMap<>();
    private static final String[] keywords = {
            "any ",
            "bool ",
            "byte ",
            "short ",
            "int ",
            "long ",
            "float ",
            "double ",
            "string ",
            "function ",
            "in ",
            "void ",
            "as ",
            "version ",
            "if ",
            "else ",
            "for ",
            "while ",
            "return ",
            "var ",
            "val ",
            "null ",
            "true ",
            "false ",
            "import ",
    };

    static {
        GLOBALS.put("recipe", "crafttweaker.recipes.IRecipeManager");
        GLOBALS.put("furnace", "crafttweaker.recipes.IFurnaceManager");
        GLOBALS.put("oreDict", "crafttweaker.oredict.IOreDict");
        GLOBALS.put("game", "crafttweaker.game.IGame");
    }

    public ZsCompletion() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(ZsTypes.IDENTIFIER), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                if (parameters.getOriginalFile() instanceof ZsFile) {
                    PsiElement lastElement = parameters.getOriginalFile().findElementAt(parameters.getOffset() - 1);

                    if (lastElement != null)
                        System.out.println("last: " + lastElement.getClass().getSimpleName() + ", Text: " + lastElement.getNode().getText());

                    boolean isDot = lastElement != null && lastElement.getText().equals(".");
                    System.out.println("isDot: " + isDot);

                    List<ZsVariable> properties = ZsUtil.findVariables((ZsFile) parameters.getOriginalFile());
                    Set<String> alreadyAdded = new HashSet<>();

                    if (!isDot) {
                        for (final ZsVariable property : properties) {
                            if (property.getName() != null && property.getName().length() > 0) {
                                if (!alreadyAdded.contains(property.getName())) {
                                    alreadyAdded.add(property.getName());

                                    result.addElement(LookupElementBuilder.create(property));
                                }
                            }
                        }

                        for (String keyword : keywords) {
                            result.addElement(LookupElementBuilder.create(keyword));
                        }

                        for (ZsClass zsClass : ZsClass.getAll()) {
                            result.addElement(zsClass.toLookupElement());
                        }

                        for (Map.Entry<String, String> entry : GLOBALS.entrySet()) {
                            result.addElement(LookupElementBuilder.create(entry.getValue(), entry.getKey())
                                    .withTypeText(entry.getValue(), true)
                                    .withInsertHandler(((insertionContext, item) -> {
                                        if (insertionContext.getFile() instanceof ZsFile) {
                                            ZsFile zs = (ZsFile) insertionContext.getFile();
                                            ZsImportList importList = ZsUtil.findImportList(zs);
                                            if (importList instanceof ZsImportListImpl && !ZsUtil.containsImport(entry.getValue(), importList)) {
                                                importList.getNode().addLeaf(ZsTypes.IMPORT_STATEMENT, "\nimport " + entry.getValue() + ";", null);
                                            }
                                        }
                                    })));
                        }
                    } else {
                        StringBuilder fullReference = new StringBuilder();
                        PsiElement element = lastElement;
                        int offset = parameters.getOffset() - 1;
                        while (element != null && !element.getText().contains("\n")) {

                            fullReference.insert(0, element.getText());

                            offset -= element.getTextLength();

                            PsiElement newElement = parameters.getOriginalFile().findElementAt(offset);
                            if (newElement != element)
                                element = newElement;
                        }
                        fullReference.deleteCharAt(fullReference.length() - 1);

                        System.out.println("Search for " + fullReference);

                        Pair<ZsClass, Boolean> pair = ZenScript.getZenClassByFullReference((ZsFile) parameters.getOriginalFile(), fullReference.toString());
                        if (pair != null) {
                            ZsClass zsClass = pair.getKey();
                            System.out.println("Off: " + offset + ", ref: " + fullReference + ", class: " + (zsClass == null ? "null" : zsClass.getShortName()));
                            if (zsClass != null) {
                                addCompletion(zsClass.getMethods(), result, pair.getRight());
                                addCompletion(zsClass.getGetters(), result, pair.getRight());
                                addCompletion(zsClass.getSetters(), result, pair.getRight());
                            }
                        }
                    }
                }
            }
        });
        extend(CompletionType.CLASS_NAME, PlatformPatterns.psiElement(ZsTypes.IDENTIFIER), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                for (ZsClass zsClass : ZsClass.getAll()) {
                    result.addElement(zsClass.toLookupElement());
                }
            }
        });

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(ZsTypes.DOT), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                PsiElement currentElement = parameters.getPosition();
                System.out.println("Dot Element: " + currentElement.getClass().getSimpleName() + ", Text: " + currentElement.getNode().getText());
            }
        });
    }

    private static void addCompletion(Collection<ZsMethod> methods, CompletionResultSet results, boolean isStatic) {
        for (ZsMethod method : methods) {
            if(isStatic && !method.isStatic())
                continue;
            String methodName = method.getName();
            if (method.isType(ZsMethod.Type.METHOD))
                methodName += "()";
            results.addElement(LookupElementBuilder.create(method, methodName));
        }
    }
}
