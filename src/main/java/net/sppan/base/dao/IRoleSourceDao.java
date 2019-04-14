package net.sppan.base.dao;

import net.sppan.base.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IRoleSourceDao {
    @Select("select t.resource_id from tb_role_resource t where t.role_id=#{roleId}")
    public List<Integer> getRoleResourceList(int roleId);
}
