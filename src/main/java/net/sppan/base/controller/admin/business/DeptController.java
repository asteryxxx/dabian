package net.sppan.base.controller.admin.business;

import net.sppan.base.common.JsonResult;
import net.sppan.base.controller.BaseController;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.entity.excel.ExportDept;
import net.sppan.base.entity.test.TbDept;
import net.sppan.base.service.IDeptService;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator.Operator;
import net.sppan.base.utils.ExcelResolve;
import net.sppan.base.utils.exportUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/department/setting")
public class DeptController extends BaseController {
	@Autowired
	private IDeptService deptService;


	@RequestMapping("/index")
	public String index(ModelMap map) {
		Subject sb = SecurityUtils.getSubject();
		User logUser = (User) sb.getPrincipal();
		Set<Role> roles =logUser.getRoles();
		Role logUserrole = roles.iterator().next();
		map.put("logUserrole",logUserrole);
		return "department/setting/index";
	}

	//  department/setting/list
	@RequestMapping("/list")
	@ResponseBody
	public Page<TbDept> list() {
		String name = request.getParameter("name");
		String deptKey = request.getParameter("deptKey");
		List<String> sortProperties = new ArrayList<>();
		sortProperties.add("id");
		Sort sort1 = new Sort(Sort.Direction.ASC, sortProperties);
		getPageRequest(sort1);

		SimpleSpecificationBuilder<TbDept> builder = new SimpleSpecificationBuilder<TbDept>();
		if (StringUtils.isNotBlank(name)) {
			builder.add("name", Operator.likeAll.name(), name);

		}
		if (StringUtils.isNotBlank(deptKey)) {
			builder.add("deptKey", Operator.likeAll.name(), deptKey);

		}
		Page<TbDept> page = deptService.findAll(builder.generateSpecification(), getPageRequest(sort1));
		return page;
	}


	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		List<TbDept> list = deptService.findAll();
		map.put("list", list);
		return "department/setting/form";
	}


	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult delete(@PathVariable Integer id, ModelMap map) {
		try {
			deptService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id, ModelMap map) {
		TbDept dept = deptService.find(id);
		map.put("dept", dept);

		List<TbDept> list = deptService.findAll();
		map.put("list", list);
		return "department/setting/form";
	}


	@RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(TbDept tbDept, ModelMap map) {
		if (tbDept.getParent().getId() == 2) {
			System.out.println("11==");
			tbDept.setParent(null);
		}
		try {
			deptService.saveOrUpdate(tbDept);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}


	@RequestMapping(value = {"/export"}, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult export(@RequestParam("ids") String[] ids, ModelMap map, HttpServletResponse response) throws Exception {

		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String localName = "部门";
		response.setHeader("Content-Disposition", "attachment;filename=" + localName);
		try {
			List<ExportDept> list = new ArrayList<>();
			for (int i = 0; i < ids.length; i++) {
				TbDept dept = deptService.find(Integer.parseInt(ids[i]));

				ExportDept ed = new ExportDept();
				ed.setXuhao(i + 1);
				BeanUtils.copyProperties(dept, ed);
				list.add(ed);
			}
			List<String> lietitles = new ArrayList<>();
			lietitles.add("序号");
			lietitles.add("部门名字");
			lietitles.add("部门标识");
			lietitles.add("创建时间");
			List<String> ziduanes = new ArrayList<>();
			ziduanes.add("name");
			ziduanes.add("deptKey");
			ziduanes.add("createTime");
			exportUtils.createExcel(list, lietitles, ziduanes, lietitles.size(), "部门信息");
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	//查看部门
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public String query(@PathVariable Integer id, ModelMap map) {
		List<TbDept> list = deptService.findAll();
		map.put("list", list);

		TbDept dept = deptService.find(id);
		map.put("dept", dept);
		map.put("zt", 1);
		return "department/setting/form";
	}

	@RequestMapping(value = {"/import"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> importfile(@RequestParam("mutipartfile") MultipartFile mutipartfile, ModelMap map) throws Exception {
		System.out.println("fileName：" + mutipartfile.getName());
		Map<String, Object> result = new HashMap<>();
        File f1=null;
		f1=deptService.acceptFile(mutipartfile);
		ExcelResolve xx=new ExcelResolve();
		List<String> list = xx.readExcel(f1);
		try {
			deptService.fzEntityesTBatchDept(list);
			result.put("status", 200);
		} catch (Exception e) {
			result.put("status", 500);
			e.printStackTrace();
		}
		return result;
	}


}
