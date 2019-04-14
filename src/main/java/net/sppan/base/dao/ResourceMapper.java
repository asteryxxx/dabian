package net.sppan.base.dao;

import net.sppan.base.entity.ResourceExt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceMapper {
    public List<ResourceExt> selectList(int roleId);
}
