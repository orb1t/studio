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
package com.intellij.ui.treeStructure.treetable;

import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.tree.WideSelectionTreeUI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * author: lesya
 */
public class TreeTableTree extends Tree {
  private Border myBorder;
  private final TreeTable myTreeTable;
  private int myVisibleRow;
  private boolean myCellFocused;


  public TreeTableTree(TreeModel model, TreeTable treeTable) {
    super(model);
    myTreeTable = treeTable;
    setCellRenderer(getCellRenderer());
    putClientProperty(WideSelectionTreeUI.TREE_TABLE_TREE_KEY, Boolean.TRUE);
  }

  public TreeTable getTreeTable() {
    return myTreeTable;
  }

  public void updateUI() {
    super.updateUI();
    TreeCellRenderer tcr = super.getCellRenderer();
    if (tcr instanceof DefaultTreeCellRenderer) {
      DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer)tcr;
      dtcr.setTextSelectionColor(UIUtil.getTableSelectionForeground());
      dtcr.setBackgroundSelectionColor(UIUtil.getTableSelectionBackground());
    }
  }

  @Override
  protected final boolean isWideSelection() {
    return UIUtil.isUnderDarcula() || UIUtil.isUnderIntelliJLaF();
  }

  public void setRowHeight(int rowHeight) {
    if (rowHeight > 0) {
      super.setRowHeight(rowHeight);
      if (myTreeTable != null && myTreeTable.getRowHeight() != rowHeight) {
        myTreeTable.setRowHeight(getRowHeight());
      }
    }
  }

  public void setBounds(int x, int y, int w, int h) {
    super.setBounds(x, 0, w, myTreeTable.getHeight());
  }

  public void paint(Graphics g) {
    putClientProperty("JTree.lineStyle", "None");
    Graphics g1 = g.create();
    g1.translate(0, -myVisibleRow * getRowHeight());
    super.paint(g1);
    g1.dispose();
    if (myBorder != null){
      myBorder.paintBorder(this, g, 0, 0, myTreeTable.getWidth(), getRowHeight());
    }
  }

  public void setBorder(Border border) {
    super.setBorder(border);
    myBorder = border;
  }

  public void setTreeTableTreeBorder(Border border) {
    myBorder = border;
  }

  public void setVisibleRow(int row) {
    myVisibleRow  = row;
    final Rectangle rowBounds = getRowBounds(myVisibleRow);
    final int indent = rowBounds.x - getVisibleRect().x - getTreeColumnOffset();
    setPreferredSize(new Dimension(getRowBounds(myVisibleRow).width + indent, getPreferredSize().height));
  }

  public void _processKeyEvent(KeyEvent e){
    super.processKeyEvent(e);
  }

  public void setCellFocused(boolean focused) {
    myCellFocused = focused;
  }

  public void setCellRenderer(final TreeCellRenderer x) {
    super.setCellRenderer(
        new TreeCellRenderer() {
          public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                        boolean selected, boolean expanded,
                                                        boolean leaf, int row, boolean hasFocus) {
            return x.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, myCellFocused);
          }
        }
    );
  }

  @Nullable
  @Override
  public Rectangle getPathBounds(TreePath path) {
    Rectangle bounds = super.getPathBounds(path);
    if (bounds == null) {
      return null;
    }
    bounds.x += getTreeColumnOffset();
    return bounds;
  }

  public int getTreeColumnOffset() {
    int dx = 0;
    for (int i = 0; i < myTreeTable.getColumnCount(); i++) {
      if (myTreeTable.isTreeColumn(i)) {
        break;
      }
      dx += myTreeTable.getColumnModel().getColumn(i).getWidth();
    }
    return dx;
  }
}
