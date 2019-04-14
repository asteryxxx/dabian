package net.sppan.base.dao;

import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.cost.CostType;
import net.sppan.base.entity.test.TbPost;
import org.springframework.stereotype.Repository;

@Repository
public interface ICosttypeDao extends IBaseDao<CostType, Integer> {

}
