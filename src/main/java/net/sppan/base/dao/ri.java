package net.sppan.base.dao;


import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.ToString;
import net.sppan.base.entity.Resource;
import net.sppan.base.entity.Role;
import net.sppan.base.entity.condition.ConditionRoleMan;
import net.sppan.base.entity.cost.CostType;
import net.sppan.base.entity.excel.ExportDept;
import net.sppan.base.entity.pachong.Weather;
import net.sppan.base.entity.pachong.WeatherResult;
import net.sppan.base.entity.support.AddressResult;
import net.sppan.base.entity.test.*;
import net.sppan.base.service.IApplyMoneyService;
import net.sppan.base.service.ICosttypeService;
import net.sppan.base.service.IPostService;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator;
import net.sppan.base.utils.*;
import net.sppan.base.vo.ZtreeView;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ri {
    @Test
    public void test1(){
        Date value=new Date();
        System.out.println(value);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 对象的属性的访问权限设置为可访问
        String sj = sdf.format(value);
        System.out.println(sj);
    }

    // 通过属性获取传入对象的指定属性的值
    public  String getValueByPropName(Object t, String propName)  {
        String value = null;
        try {
            // 通过属性获取对象的属性
            Field field = t.getClass().getDeclaredField(propName);
            System.out.println("类型---"+field.getGenericType());

            //判断属性类型是不是时间类型是就转成字符串
            if (field.getGenericType().toString().equals(
                    "class java.util.Date")){
                System.out.println("是时间类型的---");
            }
            // 对象的属性的访问权限设置为可访问
            field.setAccessible(true);

            // 获取属性的对应的值
            value = field.get(t).toString();
            System.out.println("值="+value);
        } catch (Exception e) {
            return null;
        }

        return value;
    }

    @Test
    public void read()throws Exception{
        File file = new File("D:\\scdwj.xlsx");
        ExcelResolve xx=new ExcelResolve();
        List<String> list = xx.readExcel(file);
        for(int i=0;i<list.size();i++) {
            String str = list.get(i);
            String[] tt = str.split(",");
            String deptname = tt[0];
            String deptkey = tt[1];
            String createtime = tt[2];
            ExportDept ed = new ExportDept();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(createtime);
            ed.setName(deptname);
            ed.setDeptKey(deptkey);
            ed.setCreateTime(date);
            System.out.println(ed.toString());
        }
     /*   Workbook book = new XSSFWorkbook(file);
        Sheet sheet = book.getSheetAt(0);
        int rowStart = sheet.getFirstRowNum();	// 首行下标
        int rowEnd = sheet.getLastRowNum();	// 尾行下标
        // 如果首行与尾行相同，表明只有一行，直接返回空数组
        if (rowStart == rowEnd) {
            book.close();
            return ;
        }
        // 获取第一行JSON对象键
        Row firstRow = sheet.getRow(rowStart);
        int cellStart = firstRow.getFirstCellNum();
        int cellEnd = firstRow.getLastCellNum();
      List<String> list=null;

        JSONArray array = new JSONArray();
        for(int i = 2; i <= rowEnd ; i++) {
            Row eachRow = sheet.getRow(i);
            if(list==null) {
                list = new ArrayList<>();
            }
                StringBuffer sb=null;
            //排除掉第一列的序号
            for (int k = 1; k < cellEnd; k++) {
                if(sb==null){
                    sb=new StringBuffer();
                }
                if (eachRow != null) {
                    String val = getValue(eachRow.getCell(k), i, k, book, false);
                        sb.append(val+",");
                        //财务部-finance_dept-2019-03-13 00:00:00-
                   }
                   if(k==cellEnd-1) {
                       list.add(sb.toString());
                       sb=null;
                   }
                }
            }*/

        /*  for(int i=0;i<list.size();i++){
              String str = list.get(i);
              String[] tt = str.split(",");
              String deptname = tt[0];
              String deptkey=tt[1];
              String createtime=tt[2];
              ExportDept ed=new ExportDept();
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              Date date = sdf.parse(createtime);
              ed.setName(deptname);ed.setDeptKey(deptkey);ed.setCreateTime(date);
              System.out.println(ed.toString());
          }*/
        }

        @Autowired
        IDeptDao postDao;
      @Autowired
      IPostService postService;
    @Test
    public  void testpost(){
            List<TbPostExt> list = postService.findbycondition("人事", "文");
        for (TbPostExt ex: list
             ) {
            System.out.println(ex.toString());
            
        }
    }

    @Autowired
    RoleManMapper roleManMapper;

    @Test
    public void testreoleman(){
        ConditionRoleMan conditionRoleMan=new ConditionRoleMan();
        conditionRoleMan.setRoleId(3);
        List<RolemanExt> list = roleManMapper.selectExistManlistByroleid(conditionRoleMan);
        List<RolemanExt> list2 = roleManMapper.selectNoExistManlistByroleid(conditionRoleMan);

        System.out.println(list.size());
        System.out.println(list2.size());

    }

    @Autowired
    PostManMapper postManMapper;

    @Test
    public void testposteman(){
        ConditionRoleMan conditionRoleMan=new ConditionRoleMan();
        conditionRoleMan.setPostId(6);
      //  List<PostmanExt> list = postManMapper.selectListBypostman(conditionRoleMan);
        List<PostmanExt> list = postManMapper.selectExistManlistBypostid(conditionRoleMan);
        List<PostmanExt> list2 = postManMapper.selectNoExistManlistBypostid(conditionRoleMan);

        System.out.println(list.size());
       System.out.println(list2.size());

    }

    @Autowired
    IPostDao ppdao;
    @Test
    public void testtime(){
        Integer i = postManMapper.findPostManbyManId(13);
        System.out.println(i);
    }
    @Autowired
    IDeptDao deptDao;
    @Autowired
    DeptManMapper deptManMapper;

    @Autowired
    IWorkmanDao workmanDao;


    @Autowired
    ICosttypeService costtypeService;
    @Test
    public  void testcost(){
        List<CostType> list = costtypeService.findAll();
        System.out.println(list.size());
    }

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    ProcessEngine processEngine;

    @Test
    public void deploy(){
        DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment();

        deploymentBuilder.addClasspathResource("activi/zhh.bpmn");
        deploymentBuilder.addClasspathResource("activi/zhh.png");
        Deployment deployment = deploymentBuilder.deploy();
    }

    @Test
    public  void gzl(){
        Map<String, Object> var=new HashMap<>();
        var.put("loginUser", "zhh");
        var.put("money", "6000");
        //根据流程定义的key（标识）来启动一个实例，activiti找该key下版本最高的流程定义
        //一般情况下为了方便开发使用该方法启动一个流程实例
		String processDefinitionKey = "mybxsq";
		  String businesskey="55555555";
		ProcessInstance pi = processEngine.getRuntimeService()
                  .startProcessInstanceByKey(processDefinitionKey,businesskey,var);

		  System.out.println("流程实例ID:"+pi.getId());
        String shiliid = pi.getId();
		  System.out.println("流程ID:"+pi.getProcessDefinitionId());

        Task task = this.processEngine.getTaskService().createTaskQuery().processInstanceId(shiliid).active().singleResult();
        System.out.println(task.getId());

       /* TaskQuery query = processEngine.getTaskService().createTaskQuery();
        query.taskAssignee("lixun");
        List<Task> list = query.list();
        for (Task task : list) {
            System.out.println(task.getId());
        }*/

    }
    @Test
    public void complete() {
      //  taskService.complete("7506");
        /*Task task = this.processEngine.getTaskService().createTaskQuery().processInstanceId("7501").active().singleResult();
        System.out.println(task.getId());*/
        List<TbUserApplymoney> list = tbUserApplymoneyDao.findByAcceptname("lixun");
        System.out.println(list.size());
    }
    @Autowired
    ITbUserApplymoneyDao tbUserApplymoneyDao;

    @Test
    public void delete() {
        List<Deployment> list = processEngine.getRepositoryService().createDeploymentQuery().list();
        for (Deployment deployment : list) {
            processEngine.getRepositoryService().deleteDeployment(deployment.getId(), true);
        }
    }


    @Test
    public void lsit(){
       /* SimpleSpecificationBuilder<TbUserApplymoney> builder = new SimpleSpecificationBuilder<TbUserApplymoney>();
       String searchText = "吃";
       if(StringUtils.isNotBlank(searchText)){
          builder.add("formname", SpecificationOperator.Operator.likeAll.name(), "吃");
            builder.add("status", SpecificationOperator.Operator.eq.name(), 1);
        builder.add("costtype", SpecificationOperator.Operator.eq.name(), 2);

        }
        Page<TbUserApplymoney> page = applyMoneyService.findAll(builder.generateSpecification(),null);*/
        TaskQuery query = processEngine.getTaskService().createTaskQuery();
        query.taskAssignee("狗贤");
        List<Task> list = query.list();
        for (Task task : list) {

            System.out.println(task.getExecutionId());
        }

       /* ProcessInstance qq = runtimeService.createProcessInstanceQuery()
                .processInstanceId("27501")
                .singleResult();
        System.out.println(qq.getBusinessKey());*/
        // 执行对象查询
        List<Execution> executionList = runtimeService.createExecutionQuery()
                .listPage(0,100);
    }

    @Autowired
    IApplyMoneyService applyMoneyService;

    @Autowired
    IResourceDao resourceDao;
    @Autowired
    IRoleDao roleDao;

    @Test
    public void bohui() {

    }
      @Autowired
    RestTemplate restTemplate;

      @Test
      public void testdizhi()throws Exception{
          List<Weather> cityname = GetShuiWenDataService.getRiverInfoList("hefei");
          System.out.println(cityname.size());

      /*    List<PicResult> list = applyMoneyMapper.findApplybyDate(7);
          System.out.println(list.size());*/
      }

    @Autowired
    ApplyMoneyMapper  applyMoneyMapper;

      //cWAxLWqDSskWsVm2hIdRdWcX38RvgBab
}

