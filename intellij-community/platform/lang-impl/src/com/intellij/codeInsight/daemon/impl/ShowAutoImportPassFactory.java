// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.codeInsight.daemon.impl;

import com.intellij.codeHighlighting.*;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ShowAutoImportPassFactory implements TextEditorHighlightingPassFactory, TextEditorHighlightingPassFactoryRegistrar {
  @Override
  public void registerHighlightingPassFactory(@NotNull TextEditorHighlightingPassRegistrar registrar, @NotNull Project project) {
    registrar.registerTextEditorHighlightingPass(this, new int[]{Pass.UPDATE_ALL,}, null, false, -1);
  }

  @Override
  @Nullable
  public TextEditorHighlightingPass createHighlightingPass(@NotNull PsiFile file, @NotNull final Editor editor) {
    if (BaseIntentionAction.canModify(file)) {
      TextRange textRange = FileStatusMap.getDirtyTextRange(editor, Pass.UPDATE_ALL);
      return textRange != null ? new ShowAutoImportPass(file.getProject(), file, editor) : null;
    }
    return null;
  }
}
