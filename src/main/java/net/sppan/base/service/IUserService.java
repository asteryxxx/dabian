package net.sppan.base.service;

import net.sppan.base.entity.User;
import net.sppan.base.entity.pachong.Weather;
import net.sppan.base.service.support.IBaseService;

import java.util.List;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
public interface IUserService extends IBaseService<User, Integer> {

	/**
	 * 根据用户名查找用户
	 * @param username
	 * @return
	 */
	User findByUserName(String username);

	/**
	 * 增加或者修改用户
	 * @param user
	 */
	void saveOrUpdate(User user);

	/**
	 * 给用户分配角色
	 * @param id 用户ID
	 * @param roleIds 角色Ids
	 */
	void grant(Integer id, String[] roleIds);
	//传城市简称爬取数据
	List<Weather> findBycity(String cityname);
	//根据登陆的id获取城市
	List<String> findcitybyUserId(String UserIP);

}
