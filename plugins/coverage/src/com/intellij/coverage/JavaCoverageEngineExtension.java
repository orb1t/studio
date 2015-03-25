/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
package com.intellij.coverage;

import com.intellij.coverage.view.JavaCoverageViewExtension;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.rt.coverage.data.LineData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Allows to customize the Java coverage engine for other JVM-based languages.
 *
 * @author anna
 * @since 2/14/11
 */
public abstract class JavaCoverageEngineExtension {
  public static final ExtensionPointName<JavaCoverageEngineExtension> EP_NAME = ExtensionPointName.create("com.intellij.javaCoverageEngineExtension");

  public abstract boolean isApplicableTo(@Nullable RunConfigurationBase conf);

  /**
   * Calculates the qualified names of class files generated from a source file.
   *
   * @param sourceFile a source file for which the information is requested.
   * @param classes    the classes contained in the file, as returned by {@link com.intellij.psi.PsiClassOwner#getClasses()}
   * @param names      the set into which the qualified names (dot-separated) of the classes need to be placed.
   * @return true if results were provided, false if this file isn't handled by this run configuration.
   */
  public boolean suggestQualifiedName(@NotNull PsiFile sourceFile, PsiClass[] classes, Set<String> names) {
    return false;
  }

  /**
   * Collects the output .class files generated from a specific source file.
   *
   * @param srcFile    the source file.
   * @param output     the output root for the module containing the source file.
   * @param testoutput the test output root for the module containing the source file.
   * @param suite      the coverage suite for which the information is being requested.
   * @param classFiles the set to be filled with class files produced from this source file.
   * @return true if the extension has filled the file list, false if this extension doesn't handle this file type.
   */
  public boolean collectOutputFiles(@NotNull final PsiFile srcFile,
                                    @Nullable final VirtualFile output,
                                    @Nullable final VirtualFile testoutput,
                                    @NotNull final CoverageSuitesBundle suite,
                                    @NotNull final Set<File> classFiles){
    return false;
  }

  /**
   * Returns the text to show when clicking on the coverage gutter. The text usually contains the number of true and false branch hits
   * for conditional statements.
   *
   * @param editor the editor in which the gutter is displayed.
   * @param file the file shown in the editor.
   * @param lineNumber the line number which was clicked.
   * @param startOffset the start offset of that line in the PSI file.
   * @param endOffset the end offset of that line in the PSI file.
   * @param lineData the coverage data for the line.
   * @return the text to show, or null if this extension doesn't handle this file type or doesn't have any custom text to show.
   */
  public String generateBriefReport(Editor editor, PsiFile file, int lineNumber, int startOffset, int endOffset, LineData lineData) {
    return null;
  }

  /**
   * Returns true if this configuration requires the generation of a source map to match the compiled .class files to
   * corresponding sources.
   */
  public boolean isSourceMapNeeded(RunConfigurationBase runConfiguration) {
    return false;
  }

  /**
   * Returns the summary information for the specified object (other than a class or a package) shown in the coverage view.
   */
  @Nullable
  public PackageAnnotator.ClassCoverageInfo getSummaryCoverageInfo(JavaCoverageAnnotator coverageAnnotator, PsiNamedElement element) {
    return null;
  }

  /**
   * Returns true if the specified .class file needs to be completely excluded from the coverage statistics.
   *
   * @param bundle the coverage suites bundle being indexed.
   * @param classFile the class file.
   */
  public boolean ignoreCoverageForClass(CoverageSuitesBundle bundle, File classFile) {
    return false;
  }

  /**
   * Returns true if the class coverage info for the specified .class file, for which it wasn't possible to find a corresponding
   * source file, needs to be preserved and made available as {@link JavaCoverageAnnotator#getClassCoverageInfo(String)}.
   * The qualified name under which the data will be available is calculated by replacing slashes with dots in the path of the
   * .class file relative to the class output root. The statistics for such classes will be included in the statistics for
   * the package with the corresponding qualified name but will not be included in the statistics of any directories.
   *
   * @param bundle the coverage suites bundle being indexed.
   * @param classFile the class file.
   */
  public boolean keepCoverageInfoForClassWithoutSource(CoverageSuitesBundle bundle, File classFile) {
    return false;
  }

  /**
   * Returns the list of tree nodes to show in the Coverage view for a node that has the specified value.

   * @param val the value of the parent node (normally a PsiFile or PsiClass)
   * @param coverageViewExtension coverage view extension
   * @return the list of nodes to display, or null if the default logic for calculating the list of child nodes should be used.
   */
  public List<AbstractTreeNode> getCoverageViewChildrenNodes(Object val, JavaCoverageViewExtension coverageViewExtension) {
    return null;
  }

  /**
   * Returns summary string containing coverage information for a node.
   * @param annotator coverage annotator used to collect coverage information
   * @param node node to get summary for
   * @return summary string for the node, usually containing percentage of covered classes and lines, or null if no custom summary is
   * provided by the extension
   */
  @Nullable
  public String getSummaryForNode(JavaCoverageAnnotator annotator, AbstractTreeNode node) {
    return null;
  }
}
