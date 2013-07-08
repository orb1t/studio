/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zmlx.hg4idea.action;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;
import org.zmlx.hg4idea.repo.HgRepository;
import org.zmlx.hg4idea.repo.HgRepositoryImpl;
import org.zmlx.hg4idea.util.HgUtil;

import java.util.Collection;

/**
 * @author Nadya Zabrodina
 */

public class HgBranchesAction extends HgAbstractGlobalAction {

  @Override
  protected void execute(Project project, Collection<VirtualFile> repositories, @Nullable VirtualFile selectedRepo) {
    HgRepository repository;
    if (selectedRepo != null) {
      repository = HgRepositoryImpl.getInstance(selectedRepo, project, project);
    }
    else {
      VirtualFile selectedRoot = HgUtil.getRootForSelectedFile(project);
      if (selectedRoot != null) {
        repository = HgRepositoryImpl.getInstance(selectedRoot, project, project);
      }
      else {
        return;
      }
    }
    HgBranchPopup.getInstance(project, repository).asListPopup().showInFocusCenter();
  }
}
