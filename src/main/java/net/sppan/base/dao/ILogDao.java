package net.sppan.base.dao;

import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Logs;
import net.sppan.base.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface ILogDao extends IBaseDao<Logs, Integer> {

}
