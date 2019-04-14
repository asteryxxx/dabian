package net.sppan.base.dao;

import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.PostmanExt;
import net.sppan.base.entity.test.RolemanExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostManMapper {
    public List<PostmanExt> selectListBypostman(ConditionRoleMan conditionRoleMan);

    /**
     * 查询该岗位下的人员
     * @param conditionRoleMan
     * @return
     */
    public List<PostmanExt> selectExistManlistBypostid(ConditionRoleMan conditionRoleMan);
    /**
     * 查询不在该岗位下的人员
     * @param conditionRoleMan
     * @return
     */
    public List<PostmanExt> selectNoExistManlistBypostid(ConditionRoleMan conditionRoleMan);

    @Select("select post_id from tb_post_man where man_id=#{id}")
    public Integer findPostManbyManId(int id);
}
