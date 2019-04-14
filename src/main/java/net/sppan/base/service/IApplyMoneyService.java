package net.sppan.base.service;

import net.sppan.base.entity.User;
import net.sppan.base.entity.cost.CostType;
import net.sppan.base.entity.test.TbUserApplymoney;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.support.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
public interface IApplyMoneyService extends IBaseService<TbUserApplymoney, Integer> {

	String save(TbUserApplymoney tbUserApplymoney,int userId);

	public void nextTask(String proId);

	Map<String,Object> getMap(User user);

    List<String> findformByUser(String nickName);

	void pass(String id);

	public void tjbusinesskey(SimpleSpecificationBuilder<TbUserApplymoney> builder, List<String> businesskylist);

	void nopass(String id);

	List<TbUserApplymoney> findByFormkey(String formkey);

    public void editformPass(Integer id,String businesskey,TbUserApplymoney tbUserApplymoney);

    List<String> findApplyBydatenum(int sj);
	List<String> findApplyBydatename(int sj);

}
