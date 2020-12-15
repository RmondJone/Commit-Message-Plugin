package com.rmondjone.commit;

import java.util.LinkedList;
import java.util.List;

/**
 * 注释：提交类型数据设置
 * 时间：2020/12/15 0015 14:43
 * 作者：郭翰林
 */
public class DataSettings {
    private List<TypeAlias> typeAliases;

    /**
     * 注释：获取提交类型
     * 时间：2020/12/15 0015 15:00
     * 作者：郭翰林
     *
     * @return
     */
    public List<TypeAlias> getTypeAliases() {
        if (typeAliases == null) {
            typeAliases = new LinkedList<>();
            typeAliases.add(new TypeAlias("新增功能", "新的功能点、新的需求"));
            typeAliases.add(new TypeAlias("Bug修复", "修复的Bug:现网发散Bug、测试阶段的Bug、验收阶段的Bug"));
            typeAliases.add(new TypeAlias("代码完善", "开发自测过程中遗漏的逻辑"));
            typeAliases.add(new TypeAlias("文档修改", "只是修改了文档:注释、README.md等"));
            typeAliases.add(new TypeAlias("样式修改", "不影响代码功能的修改:CSS样式、代码格式化等"));
            typeAliases.add(new TypeAlias("代码重构", "代码更改既不修复错误也不添加功能"));
            typeAliases.add(new TypeAlias("性能优化", "代码更改可以提高性能"));
            typeAliases.add(new TypeAlias("测试代码", "添加缺失测试或更正现有测试"));
            typeAliases.add(new TypeAlias("编译代码", "影响构建系统或外部依赖项的更改:build.gradle、package.json、Podfile等"));
            typeAliases.add(new TypeAlias("持续集成", "我们的CI配置文件和脚本的更改:Jenkinsfile等"));
            typeAliases.add(new TypeAlias("回退更改", "代码回退提交更改"));
            typeAliases.add(new TypeAlias("其他提交", "除以上所有类型之外的提交更改"));
        }
        return typeAliases;
    }

    /**
     * 注释：设置提交类型
     * 时间：2020/12/15 0015 15:00
     * 作者：郭翰林
     *
     * @param typeAliases
     */
    public void setTypeAliases(List<TypeAlias> typeAliases) {
        this.typeAliases = typeAliases;
    }
}
