package net.sppan.base.controller.admin.business;

import net.sppan.base.common.JsonResult;
import net.sppan.base.controller.BaseController;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.*;
import net.sppan.base.service.IDeptService;
import net.sppan.base.service.IPostService;
import net.sppan.base.service.IRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/role/man")
public class RolemanController extends BaseController {
	@Autowired
	private IPostService postService;


	@RequestMapping("")
	public String index() {
		return "role/man/index";
	}

	@RequestMapping("/list")
	@ResponseBody
	public PageRoleManEx list(Integer pageSize,Integer pageNumber) {
		String roleMan = request.getParameter("roleMan");
		String manName = request.getParameter("manName");

		if((StringUtils.isBlank(roleMan))||(roleMan.equals(""))){
			roleMan=null;
		}
		if((StringUtils.isBlank(manName))||(manName.equals(""))){
			manName=null;
		}

		ConditionRoleMan conditionRoleMan=new ConditionRoleMan(roleMan,manName);
		List<RolemanExt> list = roleService.selectListByroleman(conditionRoleMan);
		PageRoleManEx ext=new PageRoleManEx();
		ext.setContent(list);
		ext.setTotalElements(list.size());
		ext.setTotalPages(1);
		return ext;
	}

	@Autowired
	IRoleService roleService;

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public String query(ModelMap map,Integer id) {
		Role role = roleService.find(id);
		//查询用户已经分配和未分配角色的用户
		ConditionRoleMan conditionRoleMan=new ConditionRoleMan();
		conditionRoleMan.setRoleId(id);
		List<RolemanExt> existList = roleService.selectExistManlistByroleid(conditionRoleMan);
		List<RolemanExt> noexistList = roleService.selectNoExistManlistByroleid(conditionRoleMan);
		map.put("existList", existList);
		map.put("role", role);
		map.put("noexistList", noexistList);
		return "role/man/grant";
	}



}
