package net.sppan.base.controller.admin.business;

import net.sppan.base.common.JsonResult;
import net.sppan.base.controller.BaseController;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.User;
import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.test.*;
import net.sppan.base.service.IDeptService;
import net.sppan.base.service.IPostService;
import net.sppan.base.service.IRoleService;
import net.sppan.base.service.IWorkmanService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/workman/setting")
public class WorkManController extends BaseController {



    @RequestMapping("")
    public String index(ModelMap map) {
        Subject sb = SecurityUtils.getSubject();
        User logUser = (User) sb.getPrincipal();
        Set<Role> roles =logUser.getRoles();
        Role logUserrole = roles.iterator().next();
        map.put("logUserrole",logUserrole);
        return "workman/setting/index";
    }

    @Autowired
    IDeptService deptService;

    @RequestMapping("/list")
    @ResponseBody
    public PageDeptManEx list() {
        String deptName = request.getParameter("deptName");
        String manName = request.getParameter("manName");

        if((StringUtils.isBlank(deptName))||(deptName.equals(""))){
            deptName=null;
        }
        if((StringUtils.isBlank(manName))||(manName.equals(""))){
            manName=null;
        }

        ConditionRoleMan conditionRoleMan=new ConditionRoleMan();
        conditionRoleMan.setManName(manName);conditionRoleMan.setDeptMan(deptName);
        List<DeptmanExt> list = deptService.selectListBydeptman(conditionRoleMan);
        PageDeptManEx ext=new PageDeptManEx();
        ext.setContent(list);
        ext.setTotalElements(list.size());
        return ext;
    }

    @Autowired
    IPostService postService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap map) {
       List<TbPost> postlist = postService.findAll();
        map.put("postlist", postlist);

        return "workman/setting/form";
    }
    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(TbWorkman tbWorkman , ModelMap map) {
        String strolddeptId = request.getParameter("olddeptId");
        int olddeptId=0;
        if(strolddeptId!=null) {
             olddeptId = Integer.parseInt(strolddeptId);
        }
            String oldpostIdsrt = request.getParameter("oldpostId");
        int oldpostId=0;
        if(oldpostIdsrt!=null) {
             oldpostId = Integer.parseInt(oldpostIdsrt);
        }

        String tpostId = request.getParameter("postId");
          int  postId = Integer.parseInt(tpostId);
        String tdeptId = request.getParameter("deptId");
           int deptId = Integer.parseInt(tdeptId);
        //原来部门的id

        if(tbWorkman.getWid()!=null){

            workmanService.deletewaijian( tbWorkman, olddeptId,  oldpostId);
        }
        try {
            TbWorkman nework = workmanService.saveOrUpdate(tbWorkman, deptId, postId);
            System.out.println(nework.getId()+"-------");
        } catch (Exception e) {
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    @Autowired
    IWorkmanService workmanService;

    @RequestMapping(value = {"/edit/{id}"}, method = RequestMethod.GET)
    public String editquery(@PathVariable("id")int id, ModelMap map) {
        TbWorkman man = workmanService.find(id);

        ConditionRoleMan conditionRoleMan=new ConditionRoleMan();
        conditionRoleMan.setManName(man.getName());
        List<DeptmanExt> deptlist = deptService.selectListBydeptman(conditionRoleMan);
        Integer olddeptId = deptlist.get(0).getId();
        String deptName = deptlist.get(0).getDeptman();
        //得到人员的部门id
        List<PostmanExt> tpostlist = postService.selectListBypostman(conditionRoleMan);
        Integer oldpostId = tpostlist.get(0).getId();
        //取出人员的岗位id
        List<TbPost> postlist = postService.findAll();
        map.put("postlist", postlist);
        map.put("deptName", deptName);

        map.put("olddeptId",olddeptId);
        map.put("oldpostId",oldpostId);
        map.put("man",man);
        return "workman/setting/edit";
    }

    @RequestMapping(value = "/delete/", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete( String id,String deptId, ModelMap map) {
        int idd=Integer.parseInt(id);
        int tempdeptId=Integer.parseInt(deptId);
        try {
              workmanService.delete(idd,tempdeptId);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
}
