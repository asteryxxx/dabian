package net.sppan.base.dao;

import net.sppan.base.entity.ResourceExt;
import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.RolemanExt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleManMapper {
    public List<RolemanExt> selectListByroleman(ConditionRoleMan conditionRoleMan);

    /**
     * 查询该用户下的人员
     * @param conditionRoleMan
     * @return
     */
    public List<RolemanExt> selectExistManlistByroleid(ConditionRoleMan conditionRoleMan);
    /**
     * 查询不在该用户下的人员
     * @param conditionRoleMan
     * @return
     */
    public List<RolemanExt> selectNoExistManlistByroleid(ConditionRoleMan conditionRoleMan);

}
