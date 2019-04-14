package net.sppan.base.controller.admin.business;

import net.sppan.base.common.JsonResult;
import net.sppan.base.controller.BaseController;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.entity.excel.ExportDept;
import net.sppan.base.entity.test.PageExt;
import net.sppan.base.entity.test.TbDept;
import net.sppan.base.entity.test.TbPost;
import net.sppan.base.entity.test.TbPostExt;
import net.sppan.base.service.IDeptService;
import net.sppan.base.service.IPostService;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator.Operator;
import net.sppan.base.utils.ExcelResolve;
import net.sppan.base.utils.exportUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/post/setting")
public class PostController extends BaseController {
	@Autowired
	private IPostService postService;


	@RequestMapping("/")
	public String index(ModelMap map) {
		Subject sb = SecurityUtils.getSubject();
		User logUser = (User) sb.getPrincipal();
		Set<Role> roles =logUser.getRoles();
		Role logUserrole = roles.iterator().next();
		map.put("logUserrole",logUserrole);
		return "post/setting/index";
	}

	//  department/setting/list
	@RequestMapping("/list")
	@ResponseBody
	public PageExt list() {
		String deptname = request.getParameter("deptname");
		String postname = request.getParameter("postname");

		if((StringUtils.isBlank(deptname))||(deptname.equals(""))){
			deptname=null;
		}
		if((StringUtils.isBlank(postname))||(postname.equals(""))){
			postname=null;
		}
		List<TbPostExt> list = postService.findbycondition(deptname, postname);
		PageExt ext=new PageExt();
		ext.setContent(list);
		ext.setTotalElements(list.size());
		return ext;
	}

	@Autowired
	IDeptService deptService;

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id, ModelMap map) {
		List<TbPostExt> tbPostExtlist = postService.findbycondition(null,null);
		TbPostExt huixianTBpost=new TbPostExt();
		for (TbPostExt tempext: tbPostExtlist) {
			if(tempext.getId().equals(id)){
				huixianTBpost=tempext;
			}
		}
		List<TbPost> postList = postService.findAll();
		map.put("postList", postList);

		//List<TbDept> deptlist = deptService.findAll();
		//map.put("deptlist", deptlist);

		map.put("huixianTBpost", huixianTBpost);
		return "post/setting/form";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		List<TbPost> postlist = postService.findAll();
		map.put("postlist", postlist);

		List<TbDept> deptlist = deptService.findAll();
		map.put("deptlist", deptlist);
		return "post/setting/add";
	}


	@RequestMapping(value = "/queryDeptBypostId/{postId}", method = RequestMethod.GET)
	@ResponseBody
	public TbDept queryDeptBypostId(@PathVariable("postId") Integer postId) {
		return	postService.selectDeptByPostId(postId);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult delete(@PathVariable Integer id, ModelMap map) {
		try {
			postService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}




	@RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(TbPostExt tbPostExt , ModelMap map) {
		String olddeptid = request.getParameter("olddeptid");
		int oldid = Integer.parseInt(olddeptid);
		//原来部门的id
		Integer dp = tbPostExt.getDeptId();
		try {
			postService.saveOrUpdate(tbPostExt,oldid);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	@RequestMapping(value = {"/addform"}, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult addform(TbPost tbPost ) {
		String tempdeptId = request.getParameter("deptId");
		int deptId = Integer.parseInt(tempdeptId);
		//原来部门的id
		try {
			postService.addfrom(tbPost,deptId);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

}
