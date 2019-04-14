package net.sppan.base.service;

import net.sppan.base.entity.User;
import net.sppan.base.entity.cost.CostType;
import net.sppan.base.service.support.IBaseService;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
public interface ICosttypeService extends IBaseService<CostType, Integer> {


	void saveCost(CostType costType);

	void deletecostType(Integer id);
}
