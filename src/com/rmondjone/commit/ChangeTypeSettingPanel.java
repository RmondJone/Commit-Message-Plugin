package com.rmondjone.commit;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.ToolbarDecorator;

import org.jetbrains.annotations.NotNull;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;

public class ChangeTypeSettingPanel {
    private JPanel mainPanel;
    private AliasTable aliasTable;
    private DataSettings dataSettings;

    public ChangeTypeSettingPanel(DataSettings dataSettings) {
        this.dataSettings = dataSettings;
        aliasTable = new AliasTable();
        //初始化提交类型设置表格
        mainPanel.add(
                ToolbarDecorator.createDecorator(aliasTable)
                        .setAddAction(button -> aliasTable.addAlias())
                        .setRemoveAction(button -> aliasTable.removeSelectedAliases())
                        .setEditAction(button -> aliasTable.editAlias())
                        .setMoveUpAction(anActionButton -> aliasTable.moveUp())
                        .setMoveDownAction(anActionButton -> aliasTable.moveDown())
                        .addExtraAction
                                (new AnActionButton("Reset Default CommitTypes", AllIcons.Actions.Rollback) {
                                    @Override
                                    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                                        aliasTable.reset(new DataSettings());
                                    }
                                }).createPanel(), BorderLayout.CENTER);
        //设置双击事件
        new DoubleClickListener() {
            @Override
            protected boolean onDoubleClick(MouseEvent e) {
                return aliasTable.editAlias();
            }
        }.installOn(aliasTable);
    }

    /**
     * 注释：重置提交数据
     * 时间：2020/12/15 0015 16:22
     * 作者：郭翰林
     */
    public void reset() {
        if (aliasTable != null) {
            aliasTable.reset(dataSettings);
        }
    }

    /**
     * 注释：获取编辑之后的提交类型
     * 时间：2020/12/15 0015 16:40
     * 作者：郭翰林
     *
     * @return
     */
    public List<TypeAlias> getTypeAliases() {
        List<TypeAlias> retTypeAlias = null;
        if (aliasTable != null) {
            retTypeAlias = aliasTable.getTypeAliases();
        }
        return retTypeAlias;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
