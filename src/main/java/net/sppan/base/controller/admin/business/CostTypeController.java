package net.sppan.base.controller.admin.business;

import lombok.extern.slf4j.Slf4j;
import net.sppan.base.common.JsonResult;
import net.sppan.base.controller.BaseController;
import net.sppan.base.entity.User;
import net.sppan.base.entity.cost.CostType;
import net.sppan.base.entity.test.PageExt;
import net.sppan.base.entity.test.TbDept;
import net.sppan.base.entity.test.TbPost;
import net.sppan.base.entity.test.TbPostExt;
import net.sppan.base.exception.CommonCode;
import net.sppan.base.exception.ResponseResult;
import net.sppan.base.service.ICosttypeService;
import net.sppan.base.service.IDeptService;
import net.sppan.base.service.IPostService;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Controller
@RequestMapping("/cost/type")
public class CostTypeController extends BaseController {
	@Autowired
	private ICosttypeService costtypeService;


	@RequestMapping("/index")
	public String index() {
		return "cost/type/index";
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

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		return "cost/type/form";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit( CostType costType, ModelMap map) {
		try {
			costtypeService.saveCost(costType);
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
			log.error("删除失败");
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}





}
