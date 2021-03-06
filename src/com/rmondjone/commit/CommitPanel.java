package com.rmondjone.commit;

import com.google.gson.Gson;
import com.intellij.dvcs.repo.Repository;
import com.intellij.dvcs.repo.RepositoryImpl;
import com.intellij.dvcs.repo.VcsRepositoryManager;
import com.intellij.icons.AllIcons;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBList;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CommitPanel {

    Project project;

    /**
     * 提交类型
     */
    private JComboBox mTypeComboBox;
    /**
     * 设计版本号
     */
    private JTextField mVersionField;
    /**
     * 简要描述
     */
    private JTextField mSimpleField;
    /**
     * 详细描述
     */
    private JTextArea mDetailField;
    /**
     * 弹框主面板
     */
    private JPanel mainPanel;
    /**
     * 提交版本历史按钮
     */
    private JButton mVersionHistoryButton;
    /**
     * 分支号作为修改版本选项
     */
    private JCheckBox branchCheckBox;
    /**
     * 版本历史记录Pop
     */
    private JBPopup popup;
    /**
     * 版本历史记录
     */
    private List<String> versionFieldList = new ArrayList<>(2);
    /**
     * 注释：提交类型模板
     * 时间：2020/12/15 0015 15:59
     * 作者：郭翰林
     */
    private DataSettings dataSettings;

    /**
     * 工程名
     */
    private String projectName;

    public CommitPanel(Project project) {
        //获取当前分支名
        Collection<Repository> repositories = VcsRepositoryManager.getInstance(project).getRepositories();
        String currentBranchName = null;
        if (!repositories.isEmpty()) {
            currentBranchName = ((RepositoryImpl) ((ArrayList) repositories).get(0)).getCurrentBranchName();
        }
        projectName = project.getName();
        //获取提交类型模板数据
        String changeTypesJson = PropertiesComponent.getInstance().getValue("ChangeTypes");
        if (!StringUtil.isEmpty(changeTypesJson)) {
            Gson gson = new Gson();
            dataSettings = gson.fromJson(changeTypesJson, DataSettings.class);
        } else {
            dataSettings = new DataSettings();
        }
        //设置提交类型默认值
        for (TypeAlias type : dataSettings.getTypeAliases()) {
            mTypeComboBox.addItem(type);
        }
        //设置提交版本默认值
        branchCheckBox.setSelected(PropertiesComponent.getInstance().getBoolean("BranchCheckBox", true));
        String finalCurrentBranchName = currentBranchName;
        branchCheckBox.addChangeListener(e -> {
            if (branchCheckBox.isSelected() && !StringUtil.isEmpty(finalCurrentBranchName)) {
                mVersionField.setText(finalCurrentBranchName);
            }
        });
        if (branchCheckBox.isSelected() && !StringUtil.isEmpty(currentBranchName)) {
            mVersionField.setText(currentBranchName);
        } else {
            String lastVersion = PropertiesComponent.getInstance().getValue("VersionField");
            mVersionField.setText(lastVersion);
        }
        String[] versionFields = PropertiesComponent.getInstance().getValues(projectName);
        if (versionFields != null) {
            versionFieldList = Arrays.asList(versionFields);
            Collections.reverse(versionFieldList);
        }
        //初始化提交版本历史按钮
        mVersionHistoryButton.setIcon(AllIcons.Vcs.History);
        mVersionHistoryButton.setText("");
        mVersionHistoryButton.addActionListener(e -> {
            popup = creatVersionHistoryChoosePop(versionFieldList);
            Dimension dimension = popup.getContent().getPreferredSize();
            popup.setMinimumSize(new Dimension(160, dimension.height));
            popup.showUnderneathOf(mVersionHistoryButton);
        });
    }

    /**
     * 注释：创建版本历史选择pop悬浮窗
     * 时间：2020/7/10 0010 11:47
     * 作者：郭翰林
     *
     * @param list
     * @return
     */
    public JBPopup creatVersionHistoryChoosePop(List<String> list) {
        JBList<String> jbList = new JBList<>();
        jbList.setListData(list.toArray(new String[list.size()]));
        PopupChooserBuilder popupChooserBuilder = new PopupChooserBuilder(jbList);
        return popupChooserBuilder.setItemChoosenCallback(() -> mVersionField.setText(jbList.getSelectedValue())).createPopup();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * 注释：保存提交历史版本
     * 时间：2020/7/10 0010 15:43
     * 作者：郭翰林
     */
    public void saveVersionFieldList() {
        List<String> saveVersionFieldList = new ArrayList<>(versionFieldList);
        Collections.reverse(saveVersionFieldList);
        String version = mVersionField.getText().trim();
        if (!branchCheckBox.isSelected()) {
            PropertiesComponent.getInstance().setValue("VersionField", version);
        }
        PropertiesComponent.getInstance().setValue("BranchCheckBox", branchCheckBox.isSelected() + "");
        if (!saveVersionFieldList.contains(version)) {
            saveVersionFieldList.add(version);
            //只保留最近10次提交版本记录
            if (saveVersionFieldList.size() > 10) {
                List<String> subSaveVersionFieldList = saveVersionFieldList.subList(saveVersionFieldList.size() - 10, saveVersionFieldList.size());
                PropertiesComponent.getInstance().setValues(projectName, subSaveVersionFieldList.toArray(new String[10]));
            } else {
                PropertiesComponent.getInstance().setValues(projectName, saveVersionFieldList.toArray(new String[versionFieldList.size() + 1]));
            }
        }
    }

    public CommitMessage getCommitMessage() {
        saveVersionFieldList();
        return new CommitMessage(
                (TypeAlias) mTypeComboBox.getSelectedItem(),
                mVersionField.getText().trim(),
                mSimpleField.getText().trim(),
                mDetailField.getText().trim());
    }
}
