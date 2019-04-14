package net.sppan.base.controller.admin;
import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSON;
import net.sppan.base.dao.IUserDao;
import net.sppan.base.dao.IUserRoleDao;
import net.sppan.base.dao.ResourceMapper;
import net.sppan.base.entity.Resource;
import net.sppan.base.entity.ResourceExt;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.entity.pachong.Weather;
import net.sppan.base.service.IResourceService;
import net.sppan.base.service.IRoleResourceService;
import net.sppan.base.service.IUserRoleService;
import net.sppan.base.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import net.sppan.base.controller.BaseController;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AdminIndexController extends BaseController{

	@Autowired
	IResourceService iResourceService;
	@Autowired
	IRoleResourceService iRoleResourceService;
	@Autowired
	IUserRoleService iUserRoleService;
	@RequestMapping(value ={"/admin/","/admin/index"})
	public String index(ModelMap map){
		Subject sb = SecurityUtils.getSubject();
		User user = (User) sb.getPrincipal();
		List<Integer> Roleidlist = iUserRoleService.getUserRoleList(user.getId());
		List<ResourceExt> newList=new ArrayList<>();
		//获取到该用户的所有角色id
		for (Integer roleId:Roleidlist
				) {
			List<ResourceExt> listre = iResourceService.selectList(roleId);
			//因为一个用户可能有多个角色，所以要遍历到新的集合里
			newList.addAll(listre);
		}
		map.put("newList", newList);
		return "admin/index";
	}

	@Autowired
	IUserService userService;
	@RequestMapping(value = {"/admin/welcome"})
	public String welcome(HttpServletRequest request,ModelMap map){
		String testip = request.getRemoteAddr();
		List<String> strList = userService.findcitybyUserId("218.22.32.186");
		List<Weather> weatherList = userService.findBycity(strList.get(1));
		map.put("weatherList",weatherList);
		//转换提日期输出格式
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String jintian = sf.format(c.getTime());
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day-1);
		String zuotian =sf.format(c.getTime());
		c.add(Calendar.DAY_OF_MONTH, 2);
		String houtian =sf.format(c.getTime());
		map.put("jintian",jintian);		map.put("zuotian",zuotian);
		map.put("houtian",houtian);
		map.put("weizhi",strList.get(0));
		return "admin/welcome";
	}
	@ResponseBody
	@RequestMapping(value = {"/admin/test"})
	public Map<String, Object> tt(){
	//	ResourceExt rs = resourceMapper.selectList();
		Subject sb = SecurityUtils.getSubject();
		User user = (User) sb.getPrincipal();
		List<Integer> Roleidlist = iUserRoleService.getUserRoleList(user.getId());
		List<ResourceExt> newList=new ArrayList<>();
		//获取到该用户的所有角色id
		for (Integer roleId:Roleidlist
				) {
			List<ResourceExt> listre = iResourceService.selectList(roleId);
			//因为一个用户可能有多个角色，所以要遍历到新的集合里
			newList.addAll(listre);
		}
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("map", newList);
		return root;
	}



}
