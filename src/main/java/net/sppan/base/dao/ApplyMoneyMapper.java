package net.sppan.base.dao;

import net.sppan.base.entity.test.PicResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApplyMoneyMapper {
    public List<PicResult> findApplybyDate(int sj);
}
