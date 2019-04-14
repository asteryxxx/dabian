package net.sppan.base.dao;

import net.sppan.base.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IUserRoleDao {
    @Select("select role_id from tb_user_role t where t.user_id=#{userId}")
    List<Integer> getUserRoleList(int userId);
}
