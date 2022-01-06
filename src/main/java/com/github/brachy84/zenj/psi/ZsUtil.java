package com.github.brachy84.zenj.psi;

import com.github.brachy84.zenj.lang.ZsFileType;
import com.github.brachy84.zenj.lang.ZsClass;
import com.github.brachy84.zenj.psi.impl.ZsVariableImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ZsUtil {

    public static final Logger LOG = Logger.getLogger("ZenJ");

    public static List<ZsVariable> findVariablesAllFiles(Project project, String varName) {
        List<ZsVariable> result = new ArrayList<>();
        for (ZsVariable zsVariable : findVariablesAllFiles(project)) {
            if (Objects.equals(zsVariable.getName(), varName)) {
                result.add(zsVariable);
            }
        }

        return result;
    }

    public static List<ZsVariable> findVariablesAllFiles(Project project) {
        List<ZsVariable> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(ZsFileType.INSTANCE, GlobalSearchScope.allScope(project));

        for (VirtualFile virtualFile : virtualFiles) {
            ZsFile zsFile = (ZsFile) PsiManager.getInstance(project).findFile(virtualFile);

            if (zsFile != null) {
                Collection<ZsVariableImpl> variables = PsiTreeUtil.findChildrenOfType(zsFile, ZsVariableImpl.class);
                result.addAll(variables);
            }
        }
        return result;
    }


    public static List<ZsVariable> findVariables(ZsFile zsFile) {
        return new ArrayList<>(PsiTreeUtil.findChildrenOfType(zsFile, ZsVariable.class));
    }

    public static List<ZsVariable> findVariables(ZsFile zsFile, String varName) {
        List<ZsVariable> result = new ArrayList<>();
        for (ZsVariable zsVariable : findVariables(zsFile)) {
            if (Objects.equals(zsVariable.getName(), varName)) {
                result.add(zsVariable);
            }
        }

        return result;
    }

    @Nullable
    public static ZsImportList findImportList(ZsFile file) {
        return PsiTreeUtil.findChildOfType(file, ZsImportList.class);
    }

    public static boolean containsImport(String className, ZsImportList importList) {
        for (ZsImportStatement importStatement : importList.getImportStatementList()) {
            String text = importStatement.getText();
            //text = text.substring(0, text.length() - 1);
            if (text.contains(className)) {
                return true;
            }
        }
        return false;
    }
}
