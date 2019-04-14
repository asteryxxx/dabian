package net.sppan.base.service.impl;

import net.sppan.base.common.JsonResult;
import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.dao.ICosttypeDao;
import net.sppan.base.dao.IUserDao;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.entity.cost.CostType;
import net.sppan.base.exception.*;
import net.sppan.base.service.ICosttypeService;
import net.sppan.base.service.IRoleService;
import net.sppan.base.service.IUserService;
import net.sppan.base.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 用户账户表  服务实现类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
@Service
public class CosttypeServiceImpl extends BaseServiceImpl<CostType, Integer> implements ICosttypeService {

	@Autowired
	private ICosttypeDao costtypeDao;

	
	@Override
	public IBaseDao<CostType, Integer> getBaseDao() {
		return this.costtypeDao;
	}


	@Override
	public void saveCost(CostType costType) {
		costType.setCreateTime(new Date());
		costtypeDao.save(costType);
	}

	@Override
	public void deletecostType(Integer id) {
		CostType cost = costtypeDao.findOne(id);
		cost.setUpdateTime(new Date());
		cost.setStatus(0);
		costtypeDao.save(cost);
	}
}
