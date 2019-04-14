package net.sppan.base.dao;

import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.test.TbDept;
import net.sppan.base.entity.test.TbPost;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostDao extends IBaseDao<TbPost, Integer> {

}
