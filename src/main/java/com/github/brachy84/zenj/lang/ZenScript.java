package com.github.brachy84.zenj.lang;

import com.github.brachy84.zenj.completion.ZsCompletion;
import com.github.brachy84.zenj.psi.ZsFile;
import com.github.brachy84.zenj.psi.ZsImportList;
import com.github.brachy84.zenj.psi.ZsImportStatement;
import com.github.brachy84.zenj.psi.ZsUtil;
import com.intellij.lang.Language;
import com.intellij.openapi.util.IconLoader;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ZenScript extends Language {

    public static final ZenScript INSTANCE = new ZenScript();

    public static final Icon ICON = IconLoader.getIcon("");

    public static final Map<String, ZsPath> PACKAGES = new HashMap<>();

    private ZenScript() {
        super("ZenScript");
    }

    @Nullable
    public static ZsClass getZenClass(String fullName) {
        if(fullName == null || fullName.isEmpty())
            return null;

        if(fullName.startsWith("["))
            return ZsClass.ARRAY;

        String[] paths = fullName.split("\\.");

        if(paths.length == 0) {
            return null;
        }

        ZsPath currentPath = PACKAGES.get(paths[0]);
        if(currentPath == null) {
            System.out.println("   could not find " + paths[0]);
            return null;
        }
        else
            System.out.println("   found " + currentPath.getName());
        for(int i = 1; i < paths.length - 1; i++) {
            ZsPath old = currentPath;
            currentPath = currentPath.getPackage(paths[i]);
            if(currentPath == null || currentPath instanceof ZsClass) {
                System.out.println("   could not find " + paths[i]);
                old.printPaths();
                return null;
            }
            else
                System.out.println("   found " + currentPath.getName());
        }
        currentPath = currentPath.getPackage(paths[paths.length - 1]);
        if(currentPath instanceof ZsClass)
            return (ZsClass) currentPath;
        return null;
    }

    @Nullable
    public static String getFullClassName(ZsFile file, String shortName) {
        ZsImportList importList = ZsUtil.findImportList(file);
        if (importList == null) return null;
        for (ZsImportStatement importStatement : importList.getImportStatementList()) {
            String text = importStatement.getText();
            text = text.substring(0, text.length() - 1);
            if (text.endsWith("." + shortName)) {
                String[] parts = text.split(" ");
                return parts[parts.length - 1];
            }
        }
        return null;
    }

    @Nullable
    public static ZsClass getClassByShortName(ZsFile file, String className) {
        String fullClassName = getFullClassName(file, className);
        return fullClassName == null ? null : getZenClass(fullClassName);
    }

    @Nullable
    public static Pair<ZsClass, Boolean> getZenClassByFullReference(ZsFile file, String reference) {
        if(reference == null || reference.isEmpty())
            return null;

        String[] paths = reference.split("\\.");

        if(paths.length == 0) {
            return null;
        }

        Pair<ZsPath, Boolean> pair = resolveFirstPath(file, paths[0]);

        ZsPath currentPath = pair.getKey();

        if(currentPath != null) {
            System.out.println("Found first path " + currentPath.getName());
        }
        int i = 1;

        while (currentPath != null && !(currentPath instanceof ZsClass) && i < paths.length) {
            String path = paths[i];
            if(path.endsWith(")")) {
                path = path.substring(0, path.indexOf("("));
            }
            if(path.endsWith("]")) {
                path = path.substring(0, path.indexOf("["));
            }
            currentPath = currentPath.getPackage(path);
            i++;
        }

        if(currentPath instanceof ZsClass) {
            ZsClass currentClass = (ZsClass) currentPath;
            boolean canBeStatic = pair.getRight();

            if(i == paths.length)
                return Pair.of(currentClass, canBeStatic);

            for(; i < paths.length; i++) {
                String path = paths[i];
                if(path.endsWith(")")) {
                    path = path.substring(0, path.indexOf("("));
                }
                if(path.endsWith("]")) {
                    path = path.substring(0, path.indexOf("["));
                }
                ZsMethod method = currentClass.getAny(path, canBeStatic);
                if(method == null || method.returnsVoid())
                    return null;
                canBeStatic = false;
                System.out.println("Method: " + method.getName() + ", returns: " + method.getReturns());
                currentClass = getZenClass(method.getReturns());
                if(currentClass == null)
                    return null;
            }

            return Pair.of(currentClass, canBeStatic);

        }
        return null;
    }

    protected static void registerClass(ZsClass zsClass) {
        String[] paths = zsClass.getName().split("\\.");
        if(paths.length == 0) {
            return;
        }

        System.out.print("Register class " + zsClass.getName());

        if(paths.length == 1) {
            PACKAGES.put(paths[0], zsClass);
            return;
        }
        StringBuilder fullPath = new StringBuilder(paths[0]);
        ZsPath currentPath = getOrCreate(fullPath.toString(), fullPath.toString(), null);
        System.out.print("  " + currentPath.getShortName() + "/");
        for(int i = 1; i < paths.length - 1; i++) {
            fullPath.append(".").append(paths[i]);
            currentPath = getOrCreate(paths[i], fullPath.toString(), currentPath);
            System.out.println(currentPath.getShortName() + "/");
        }
        currentPath.registerPath(zsClass);
    }

    private static ZsPath getOrCreate(String pathName, String fullPath, @Nullable ZsPath parent) {
        if(parent == null) {
            return PACKAGES.computeIfAbsent(pathName, key -> new ZsPath(fullPath));
        }
        ZsPath path = parent.getPackage(pathName);
        if(path == null) {
            path = new ZsPath(fullPath);
            parent.registerPath(path);
        }
        return path;
    }

    private static Pair<ZsPath, Boolean> resolveFirstPath(ZsFile file, String path) {
        if(path.startsWith("<") && path.endsWith(">")) {
            String bracketHandler = path.substring(1, path.length() - 1).split(":")[0];
            switch (bracketHandler) {
                case "material": return Pair.of(getZenClass("mods.gregtech.material.Material"), false);
                case "fluid":
                case "liquid":
                    return Pair.of(getZenClass("crafttweaker.liquid.ILiquidStack"), false);
                case "ore":
                    return Pair.of(getZenClass("crafttweaker.oredict.IOreDictEntry"), false);
                default:
                    return Pair.of(getZenClass("crafttweaker.item.IItemStack"), false);
            }
        }

        String classPath = ZsCompletion.GLOBALS.get(path);
        if(classPath != null) {
            return Pair.of(getZenClass(classPath), false);
        }

        classPath = getFullClassName(file, path);
        if(classPath != null) {
            System.out.println("Search for " + classPath);
            return Pair.of(getZenClass(classPath), true);
        }

        ZsPath zsPath = PACKAGES.get(path);
        return Pair.of(zsPath, true);
    }
}
