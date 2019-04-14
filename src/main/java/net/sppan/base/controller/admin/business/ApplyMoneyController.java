package net.sppan.base.controller.admin.business;

import net.sppan.base.common.JsonResult;
import net.sppan.base.controller.BaseController;
import net.sppan.base.entity.User;
import net.sppan.base.entity.cost.CostType;
import net.sppan.base.entity.test.TbUserApplymoney;
import net.sppan.base.service.IApplyMoneyService;
import net.sppan.base.service.ICosttypeService;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator;
import net.sppan.base.utils.FindIPutils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/money/reimbursement")
public class ApplyMoneyController extends BaseController {
	@Autowired
	private ICosttypeService costtypeService;
	@Autowired
	IApplyMoneyService applyMoneyService;

	@RequestMapping("/apply")
	public String index( ModelMap map) {
		Subject sb = SecurityUtils.getSubject();
		User user = (User) sb.getPrincipal();
		List<CostType> costList = costtypeService.findAll();
		map.put("costList",costList);
		map.put("user",user);
		return "money/reimbursement/index";
	}

	@RequestMapping("/list")
	@ResponseBody
	public Page<CostType> list() {
		SimpleSpecificationBuilder<CostType> builder = new SimpleSpecificationBuilder<CostType>();
		String searchText = request.getParameter("searchText");
		if(StringUtils.isNotBlank(searchText)){
			builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
		}
		Page<CostType> page = costtypeService.findAll(builder.generateSpecification(), getPageRequest());
		return page;
	}


	@RequestMapping(value = "/addform", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult addform(TbUserApplymoney tbUserApplymoney,int userId, ModelMap map) {
		try {
			String proId = applyMoneyService.save(tbUserApplymoney, userId);
			applyMoneyService.nextTask(proId);
					} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult delete(@PathVariable Integer id, ModelMap map) {
		try {
			costtypeService.deletecostType(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	@Autowired
	ProcessEngine processEngine;

	@GetMapping("/welcome")
	@ResponseBody
	public Map<String,Object> welcome(HttpServletRequest request) {
		String ip = FindIPutils.getIpAddress(request);
		System.out.println("---"+ip);
		Map<String,Object> map=new HashMap<>();
		Subject sb = SecurityUtils.getSubject();
		User user = (User) sb.getPrincipal();
		String userName = user.getUserName();
		map=applyMoneyService.getMap(user);
		return map;
	}
	@GetMapping("/query")
	public String query( ModelMap map) {
		List<CostType> costList = costtypeService.findAll();
		map.put("costList",costList);
		return "/examine/account/query/queryindex";
	}

	@RequestMapping("/query/list")
	@ResponseBody
	public Page<TbUserApplymoney> querylist() {
		Subject sb = SecurityUtils.getSubject();
		User user = (User) sb.getPrincipal();
		SimpleSpecificationBuilder<TbUserApplymoney> builder=null;

			builder = new SimpleSpecificationBuilder<TbUserApplymoney>();
			String formname = request.getParameter("formname");
			String description = request.getParameter("description");
			String tcosttype = request.getParameter("costtype");
			int costtype = 0;
			if (StringUtils.isNotBlank(tcosttype) && !tcosttype.equals("")) {
				costtype = Integer.parseInt(tcosttype);

			}
			if (StringUtils.isNotBlank(formname) && !formname.equals("")) {
				builder.add("formname", SpecificationOperator.Operator.likeAll.name(), formname);

			}
			if (StringUtils.isNotBlank(description) && !description.equals("")) {
				builder.add("description", SpecificationOperator.Operator.likeAll.name(), description);

			}
			if (costtype != 0) {
				builder.add("costtype", SpecificationOperator.Operator.eq.name(), costtype);

			}
		if(user.getRoles().iterator().next().getId()!=1){
			builder.add("applyname", SpecificationOperator.Operator.eq.name(), user.getNickName());
		}
			Page<TbUserApplymoney> page = applyMoneyService.findAll(builder.generateSpecification(),null);
			return page;


	}

	@GetMapping("/editform/{id}")
	public String edit(@PathVariable String id, ModelMap map) {
			//传过来的是formkey
			List<TbUserApplymoney> list = applyMoneyService.findByFormkey(id);
			TbUserApplymoney userApplymoney = list.get(0);
		List<CostType> costList = costtypeService.findAll();
			//需要修改的对象
		map.put("userApplymoney",userApplymoney);
		map.put("costList",costList);
		return "examine/account/query/edit";
	}


	//重新修改提交
	@RequestMapping(value = "/editform/pass", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult editformPass(TbUserApplymoney tbUserApplymoney, ModelMap map) {
		try {
			applyMoneyService.editformPass(tbUserApplymoney.getId(), tbUserApplymoney.getFormkey(), tbUserApplymoney);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}



	@GetMapping("/querypic")
	public String querypic( ModelMap map) {

		return "/examine/account/query/querypic";
	}

	@RequestMapping(value = "/querypicbydate/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> querypicbydate(@PathVariable Integer id) {
		Map<String, Object> map=new HashMap<>();
		List<String> nameList = applyMoneyService.findApplyBydatename(id);
		List<String> numList = applyMoneyService.findApplyBydatenum(id);
		//key是name，value是数量
		map.put("nameList",nameList);
		map.put("numList",numList);
		return map;
	}
}
