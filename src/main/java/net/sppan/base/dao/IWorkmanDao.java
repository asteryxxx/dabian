package net.sppan.base.dao;

import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.test.TbDept;
import net.sppan.base.entity.test.TbWorkman;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWorkmanDao extends IBaseDao<TbWorkman, Integer> {


    @Query(value ="select max(id) from tb_workman  where id  like CONCAT(:id,'%')", nativeQuery = true)
    public Integer findByNameLike(@Param("id") int id);
}
