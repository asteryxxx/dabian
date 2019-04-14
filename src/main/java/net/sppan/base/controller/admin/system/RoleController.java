package net.sppan.base.controller.admin.system;

import net.sppan.base.common.JsonResult;
import net.sppan.base.controller.BaseController;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.test.TbWorkman;
import net.sppan.base.service.IResourceService;
import net.sppan.base.service.IRoleService;
import net.sppan.base.service.IUserService;
import net.sppan.base.service.IWorkmanService;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator.Operator;

import net.sppan.base.vo.ZtreeView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IResourceService resourceService;

	@RequestMapping(value = { "/", "/index" })
	public String index() {
		return "admin/role/index";
	}

	@RequestMapping(value = { "/list" })
	@ResponseBody
	public Page<Role> list() {
		SimpleSpecificationBuilder<Role> builder = new SimpleSpecificationBuilder<Role>();
		String searchText = request.getParameter("searchText");
		if(StringUtils.isNotBlank(searchText)){
			builder.add("name", Operator.likeAll.name(), searchText);
			builder.addOr("description", Operator.likeAll.name(), searchText);
		}
		Page<Role> page = roleService.findAll(builder.generateSpecification(), getPageRequest());
		return page;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		return "admin/role/form";
	}
	

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id,ModelMap map) {
		Role role = roleService.find(id);
		map.put("role", role);
		return "admin/role/form";
	}
	
	
	@RequestMapping(value= {"/edit"},method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(Role role,ModelMap map){
		try {
			roleService.saveOrUpdate(role);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult delete(@PathVariable Integer id,ModelMap map) {
		try {
			roleService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
	
	@RequestMapping(value = "/grant/{id}", method = RequestMethod.GET)
	public String grant(@PathVariable Integer id, ModelMap map) {
		Role role = roleService.find(id);
		map.put("role", role);
		return "admin/role/grant";
	}

	@RequestMapping(value = "/grant/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult grant(@PathVariable Integer id,
			@RequestParam(required = false) String[] resourceIds, ModelMap map) {
		try {
			roleService.grant(id,resourceIds);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}


	@Autowired
	IWorkmanService workmanService;

	@RequestMapping(value = "/grant/man/{id}", method = RequestMethod.GET)
	public String grantman(@PathVariable("id") Integer id, ModelMap map) {
		Role role = roleService.find(id);
		map.put("role", role);

		map.put("zt",2);
		return "admin/role/grant";
	}
	@RequestMapping("/grant/man/select/{id}")
	@ResponseBody
	public Map<String,Object> tree(@PathVariable("id") Integer id){
		List<TbWorkman> existlist = roleService.findexistByroleId(id);
		//查询出已经分配的人员和没有分配的显示在多选框里
		List<TbWorkman> noexistlist = roleService.findnoexistByroleId(1, existlist);
		Map<String,Object> map=new HashMap<>();
		map.put("existlist", existlist);
		map.put("noexistlist", noexistlist);
		return map;
	}


	@RequestMapping(value = "/grant/man/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult grantworkman(@PathVariable Integer id,
							@RequestParam(required = false) String[] workmanIds, ModelMap map) {
		try {
			roleService.grantworkman(id,workmanIds);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
}
