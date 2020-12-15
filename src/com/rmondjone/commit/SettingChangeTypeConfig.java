package com.rmondjone.commit;

import com.google.gson.Gson;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.text.StringUtil;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.swing.JComponent;

/**
 * 注释：提交类型类型配置
 * 时间：2020/12/15 0015 13:55
 * 作者：郭翰林
 */
public class SettingChangeTypeConfig implements SearchableConfigurable {
    private ChangeTypeSettingPanel changeTypeSettingPanel;
    private DataSettings dataSettings;

    public SettingChangeTypeConfig() {
        String changeTypesJson = PropertiesComponent.getInstance().getValue("ChangeTypes");
        if (!StringUtil.isEmpty(changeTypesJson)) {
            Gson gson = new Gson();
            dataSettings = gson.fromJson(changeTypesJson, DataSettings.class);
        } else {
            dataSettings = new DataSettings();
        }
    }

    @NotNull
    @Override
    public String getId() {
        return "plugins.commit.changType";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Commit-Message-Create";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (changeTypeSettingPanel == null) {
            changeTypeSettingPanel = new ChangeTypeSettingPanel(dataSettings);
        }
        return changeTypeSettingPanel.getMainPanel();
    }

    /**
     * 注释：复写重置方法
     * 时间：2020/12/15 0015 16:47
     * 作者：郭翰林
     */
    @Override
    public void reset() {
        if (changeTypeSettingPanel != null) {
            changeTypeSettingPanel.reset();
        }
    }

    /**
     * 注释：检测是否有修改
     * 时间：2020/12/15 0015 16:47
     * 作者：郭翰林
     *
     * @return
     */
    @Override
    public boolean isModified() {
        if (changeTypeSettingPanel != null) {
            List<TypeAlias> changeTypeAlias = changeTypeSettingPanel.getTypeAliases();
            if (changeTypeAlias != null) {
                Gson gson = new Gson();
                return !StringUtil.equals(gson.toJson(dataSettings.getTypeAliases()), gson.toJson(changeTypeAlias));
            }
        }
        return false;
    }

    /**
     * 注释：保存设置操作
     * 时间：2020/12/15 0015 16:48
     * 作者：郭翰林
     *
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        if (changeTypeSettingPanel != null) {
            List<TypeAlias> changeTypeAlias = changeTypeSettingPanel.getTypeAliases();
            if (changeTypeAlias != null) {
                Gson gson = new Gson();
                if (!StringUtil.equals(gson.toJson(dataSettings.getTypeAliases()), gson.toJson(changeTypeAlias))) {
                    dataSettings.setTypeAliases(changeTypeAlias);
                    PropertiesComponent.getInstance().setValue("ChangeTypes", gson.toJson(dataSettings));
                }
            }
        }
    }
}
