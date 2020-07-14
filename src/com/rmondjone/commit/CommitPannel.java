package com.rmondjone.commit;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.components.JBList;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CommitPannel {

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
     * 版本历史记录Pop
     */
    private JBPopup popup;
    /**
     * 版本历史记录
     */
    private List<String> versionFieldList = new ArrayList<>(2);

    public CommitPannel(Project project) {
        //设置提交类型默认值
        for (ChangeType type : ChangeType.values()) {
            mTypeComboBox.addItem(type);
        }
        //设置提交版本默认值
        String lastVersion = PropertiesComponent.getInstance().getValue("VersionField");
        mVersionField.setText(lastVersion);
        String[] versionFields = PropertiesComponent.getInstance().getValues("VersionFields");
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
        PropertiesComponent.getInstance().setValue("VersionField", version);
        if (!saveVersionFieldList.contains(version)) {
            saveVersionFieldList.add(version);
            //只保留最近10次提交版本记录
            if (saveVersionFieldList.size() > 10) {
                List<String> subSaveVersionFieldList = saveVersionFieldList.subList(saveVersionFieldList.size() - 10, saveVersionFieldList.size());
                PropertiesComponent.getInstance().setValues("VersionFields", subSaveVersionFieldList.toArray(new String[10]));
            } else {
                PropertiesComponent.getInstance().setValues("VersionFields", saveVersionFieldList.toArray(new String[versionFieldList.size() + 1]));
            }
        }
    }

    public CommitMessage getCommitMessage() {
        saveVersionFieldList();
        return new CommitMessage(
                (ChangeType) mTypeComboBox.getSelectedItem(),
                mVersionField.getText().trim(),
                mSimpleField.getText().trim(),
                mDetailField.getText().trim());
    }
}
