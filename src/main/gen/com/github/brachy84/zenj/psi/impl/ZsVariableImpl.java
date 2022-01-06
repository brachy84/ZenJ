// This is a generated file. Not intended for manual editing.
package com.github.brachy84.zenj.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.brachy84.zenj.psi.ZsTypes.*;
import com.github.brachy84.zenj.psi.*;

public class ZsVariableImpl extends ZsNamedElementImpl implements ZsVariable {

  public ZsVariableImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZsVisitor visitor) {
    visitor.visitVariable(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZsVisitor) accept((ZsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return findNotNullChildByType(IDENTIFIER);
  }

  @Override
  public String getName() {
    return ZsPsiImplUtil.getName(this);
  }

  @Override
  public PsiElement getNameIdentifier() {
    return ZsPsiImplUtil.getNameIdentifier(this);
  }

  @Override
  public PsiElement setName(String name) {
    return ZsPsiImplUtil.setName(this, name);
  }

}
