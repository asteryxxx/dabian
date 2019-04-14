package net.sppan.base.dao;

import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.test.TbDept;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeptDao extends IBaseDao<TbDept, Integer> {

}
