package net.sppan.base.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.dao.IUserDao;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.entity.pachong.Weather;
import net.sppan.base.entity.support.AddressResult;
import net.sppan.base.service.IRoleService;
import net.sppan.base.service.IUserService;
import net.sppan.base.service.support.impl.BaseServiceImpl;

import net.sppan.base.utils.GetShuiWenDataService;
import net.sppan.base.utils.PinYin4jUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 用户账户表  服务实现类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements IUserService {

	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IRoleService roleService;
	
	@Override
	public IBaseDao<User, Integer> getBaseDao() {
		return this.userDao;
	}

	@Override
	public User findByUserName(String username) {
		return userDao.findByUserName(username);
	}

	@Override
	public void saveOrUpdate(User user) {
		if(user.getId() != null){
			User dbUser = find(user.getId());
			dbUser.setNickName(user.getNickName());
			dbUser.setSex(user.getSex());
			dbUser.setBirthday(user.getBirthday());
			dbUser.setTelephone(user.getTelephone());
			dbUser.setEmail(user.getEmail());
			dbUser.setAddress(user.getAddress());
			dbUser.setLocked(user.getLocked());
			dbUser.setDescription(user.getDescription());
			dbUser.setUpdateTime(new Date());
			update(dbUser);
		}else{
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			
			user.setDeleteStatus(0);
			user.setPassword(MD5Utils.md5("111111"));
			save(user);
		}
	}
	
	

	@Override
	public void delete(Integer id) {
		User user = find(id);
		Assert.state(!"admin".equals(user.getUserName()),"超级管理员用户不能删除");
		super.delete(id);
	}

	@Override
	public void grant(Integer id, String[] roleIds) {
		User user = find(id);
		Assert.notNull(user, "用户不存在");
		Assert.state(!"admin".equals(user.getUserName()),"超级管理员用户不能修改管理角色");
		Role role;
		Set<Role> roles = new HashSet<Role>();
		if(roleIds != null){
			for (int i = 0; i < roleIds.length; i++) {
				Integer rid = Integer.parseInt(roleIds[i]);
				role = roleService.find(rid);
				roles.add(role);
			}
		}
		user.setRoles(roles);
		update(user);
	}

	@Override
	public List<Weather> findBycity(String cityname) {
        try {
            List<Weather> list=GetShuiWenDataService.getRiverInfoList( cityname);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	@Autowired
	RestTemplate restTemplate;

	@Override
	public List<String> findcitybyUserId(String UserIP) {
		List<String> stringList=new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ip", UserIP);
		paramMap.put("ak", "cWAxLWqDSskWsVm2hIdRdWcX38RvgBab");
		String url = "http://api.map.baidu.com/location/ip?ak={ak}&ip={ip}";
		ResponseEntity responseEntity = restTemplate.getForEntity(url, String.class, paramMap);
		String json= (String) responseEntity.getBody();
		JSONObject jsonObject = JSONObject.parseObject(json);
		System.out.println(jsonObject);
		AddressResult add = JSON.toJavaObject(jsonObject,AddressResult.class);
		String pronvincecity = add.getContent().getAddress();//获得广东省珠海市
		stringList.add(pronvincecity);
		String city = add.getContent().getAddress_detail().getCity().substring(0,2);//获得珠海
		String ss = PinYin4jUtils.getPingYin(city);
		stringList.add(ss);
		return stringList;
	}

}
