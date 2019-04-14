package net.sppan.base.controller.admin.business;

import net.sppan.base.common.JsonResult;
import net.sppan.base.controller.BaseController;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.*;
import net.sppan.base.service.IPostService;
import net.sppan.base.service.IRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/post/man")
public class PostmanController extends BaseController {
	@Autowired
	private IPostService postService;


	@RequestMapping("/index")
	public String index() {
		return "post/man/index";
	}

	@RequestMapping("/list")
	@ResponseBody
	public PagePostManEx list() {
		String postName = request.getParameter("postName");
		String manName = request.getParameter("manName");

		if((StringUtils.isBlank(postName))||(postName.equals(""))){
			postName=null;
		}
		if((StringUtils.isBlank(manName))||(manName.equals(""))){
			manName=null;
		}

		ConditionRoleMan conditionRoleMan=new ConditionRoleMan();
		conditionRoleMan.setManName(manName);conditionRoleMan.setPostMan(postName);
		List<PostmanExt> list = postService.selectListBypostman(conditionRoleMan);
		PagePostManEx ext=new PagePostManEx();
		ext.setContent(list);
		ext.setTotalElements(list.size());
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

	@RequestMapping(value = "/grant/{id}", method = RequestMethod.GET)
	public String grant(ModelMap map,@PathVariable("id") Integer id) {
		TbPost post = postService.find(id);
		map.put("post", post);
		return "post/man/grant";
	}



	@RequestMapping("/grant/select/{id}")
	@ResponseBody
	public Map<String,Object> tree(@PathVariable("id") Integer id){
		//查询用户已经分配和未分配角色的岗位
		ConditionRoleMan conditionRoleMan=new ConditionRoleMan();
		conditionRoleMan.setPostId(id);
		List<PostmanExt> existList = postService.selectExistManlistBypostid(conditionRoleMan);
		List<PostmanExt> noexistList = postService.selectNoExistManlistBypostid(conditionRoleMan);
		Map<String,Object> map=new HashMap<>();
		map.put("existList", existList);
		map.put("noexistList", noexistList);
		return map;
	}


	@RequestMapping(value = "/grant/postman/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult grantworkman(@PathVariable Integer id,
								   @RequestParam(required = false) String[] workmanIds, ModelMap map) {
		try {
			postService.grantworkman(id,workmanIds);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
}
