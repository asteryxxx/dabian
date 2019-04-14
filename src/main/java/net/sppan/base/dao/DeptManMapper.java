package net.sppan.base.dao;

import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.DeptmanExt;
import net.sppan.base.entity.test.PostmanExt;
import net.sppan.base.entity.test.TbDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptManMapper {
    public List<DeptmanExt> selectListBydeptman(ConditionRoleMan conditionRoleMan);
    //找到岗位对应的部门信息
    public TbDept selectDeptByPostId(int postId);
}
