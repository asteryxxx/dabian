package net.sppan.base.dao;

import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.test.TbUserApplymoney;
import net.sppan.base.entity.test.TbWorkman;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITbUserApplymoneyDao extends IBaseDao<TbUserApplymoney, Integer> {

    List<TbUserApplymoney> findByAcceptname(String acceptName);

    List<TbUserApplymoney> findByFormkey(String formkey);
}
