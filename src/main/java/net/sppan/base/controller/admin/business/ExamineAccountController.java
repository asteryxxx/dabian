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
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/examine/account")
public class ExamineAccountController extends BaseController {
	@Autowired
	private ICosttypeService costtypeService;
	@Autowired
	IApplyMoneyService applyMoneyService;

	@RequestMapping("/index")
	public String index( ModelMap map) {
		List<CostType> costList = costtypeService.findAll();
		map.put("costList",costList);
		return "/examine/account/index";
	}

	@RequestMapping("/list")
	@ResponseBody
	public Page<TbUserApplymoney> list() {
		Subject sb = SecurityUtils.getSubject();
		User user = (User) sb.getPrincipal();
		//搜索一下用户的任务如果为0 直接不走下面循环
		TaskQuery query = processEngine.getTaskService().createTaskQuery();
		query.taskAssignee(user.getNickName());
		List<Task> list = query.list();
		//需要执行的任务数量
		int zhixingrenwushuliang = list.size();
		SimpleSpecificationBuilder<TbUserApplymoney> builder=null;
		if(zhixingrenwushuliang!=0) {
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
			builder.add("status", SpecificationOperator.Operator.eq.name(), 1);

			List<String> businesskylist = applyMoneyService.findformByUser(user.getNickName());
			//根据用户名找到对应的任务，然后找到实例的busineskey对应表的流水号进行In判断
			applyMoneyService.tjbusinesskey(builder, businesskylist);

			Page<TbUserApplymoney> page = applyMoneyService.findAll(builder.generateSpecification(),null);
			return page;

		}else{
			Page<TbUserApplymoney> page=null;
			return page;
		}
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

	@RequestMapping(value = "/pass/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult pass(@PathVariable String id, ModelMap map) {
		try {
			applyMoneyService.pass(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	@RequestMapping(value = "/nopass/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult nopass(@PathVariable String id, ModelMap map) {
		try {
			applyMoneyService.nopass(id);
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
	public Map<String,Object> welcome() {
		Map<String,Object> map=new HashMap<>();
		Subject sb = SecurityUtils.getSubject();
		User user = (User) sb.getPrincipal();
		String userName = user.getUserName();
		map=applyMoneyService.getMap(user);
		return map;
	}


}
