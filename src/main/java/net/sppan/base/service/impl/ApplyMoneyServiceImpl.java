package net.sppan.base.service.impl;

import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.dao.*;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.User;
import net.sppan.base.entity.cost.CostType;
import net.sppan.base.entity.test.PicResult;
import net.sppan.base.entity.test.TbUserApplymoney;
import net.sppan.base.service.IApplyMoneyService;
import net.sppan.base.service.ICosttypeService;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator;
import net.sppan.base.service.support.impl.BaseServiceImpl;
import net.sppan.base.utils.HuituiUtils;
import net.sppan.base.utils.Randomutils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 用户账户表  服务实现类
 * </p>
 *
 * @author SPPan
 * @since 2016-12-28
 */
@Service
public class ApplyMoneyServiceImpl extends BaseServiceImpl<TbUserApplymoney, Integer> implements IApplyMoneyService{

	@Autowired
	private ITbUserApplymoneyDao tbUserApplymoneyDao;

	
	@Override
	public IBaseDao<TbUserApplymoney, Integer> getBaseDao() {
		return this.tbUserApplymoneyDao;
	}
	@Autowired
	IUserDao userDao;
	@Transactional
	public String save(TbUserApplymoney tbUserApplymoney,int userId){
		Randomutils randomutils = new Randomutils();
		String key = randomutils.getStringRandom(8);
		tbUserApplymoney.setFormkey(key);
		tbUserApplymoney.setCreateTime(new Date());
		tbUserApplymoney.setStatus(1);//待审核
		//维护外键
		User uu = userDao.findOne(userId);
		TbUserApplymoney save = tbUserApplymoneyDao.save(tbUserApplymoney);
		uu.getApplymoneyes().add(save);
		//启动流程实例
		String proId = start(key, uu.getUserName(),save.getSum());
		return proId;
	}

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	ProcessEngine processEngine;

	public String start(String businessKey,String userName,int money) {
		String processDefinitionKey = "mybxsq";
		Map<String, Object> var = new HashMap<>();
		var.put("loginUser", userName);
		var.put("businessKey", businessKey);
		var.put("money", money);
		ProcessInstance pro = runtimeService.startProcessInstanceByKey(processDefinitionKey,businessKey, var);
		return pro.getId();
	}

	public void nextTask(String proId) {
		Task task = this.processEngine.getTaskService().createTaskQuery().processInstanceId(proId).active().singleResult();
		String taskid = task.getId();
		Map<String, Object> map2=new HashMap<>();
		map2.put("proId",proId);
		taskService.complete(taskid,map2);//这里到审核了，但是要把流程实例的id存到map里
	}

	@Autowired
	IUserRoleDao userRoleDao;

	public Map<String, Object> getMap(User user) {
		String userName = user.getUserName();
		List<Integer> role = userRoleDao.getUserRoleList(user.getId());
		Integer roleId = role.get(0);
		Map<String, Object> map=new HashMap<>();
		if(roleId!=1&&roleId!=2) {
			StringBuffer sb = findfrombyUsername(userName);
			map.put("usermessage", sb.toString());
		}else{
			//获取当前登陆用户
			//根据用户角色划分，如果是12，显示需要审核和自己申请的，如果是其他普通用户就是显示 自己申请的表单
			TaskQuery query = processEngine.getTaskService().createTaskQuery();
			query.taskAssignee(user.getNickName());
			List<Task> list = query.list();

			//需要执行的任务数量
			int zhixingrenwushuliang = list.size();
			if (zhixingrenwushuliang == 0) {
				map.put("status", "200");
				map.put("taskmessage", "你当前没有需要审核的任务");
			}else {
				map.put("status", "999");
				map.put("taskmessage", "你当前需要审核的任务有:" + zhixingrenwushuliang + "个，赶紧去处理吧！！");
			}
			StringBuffer sb = findfrombyUsername(userName);
			map.put("usermessage", sb.toString());
		}
		return map;
	}

	@Override
	public List<String> findformByUser(String nickName) {
		TaskQuery query = processEngine.getTaskService().createTaskQuery();
		query.taskAssignee(nickName);
		List<Task> list = query.list();
		List<String> buskeyList=new ArrayList<>();
		for (Task task : list) {

			String shiliId = task.getExecutionId();
			String buskey = FindshiliById(shiliId);
			buskeyList.add(buskey);
		}
		return buskeyList;
	}
	@Transactional
	public void pass(String id) {
		//id是businesskey哦
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(id).singleResult();
		List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey(id).list();
		Task task = taskList.get(0);
		String taskId = task.getId();
		 processEngine.getTaskService().complete(taskId);

		List<TbUserApplymoney> form = tbUserApplymoneyDao.findByFormkey(id);
		TbUserApplymoney xiugai = form.get(0);
		xiugai.setStatus(3);
		tbUserApplymoneyDao.save(xiugai);
	}

	private String FindshiliById(String ProId) {
		ProcessInstance qq = runtimeService.createProcessInstanceQuery()
				.processInstanceId(ProId)
				.singleResult();
		return qq.getBusinessKey();
	}

	public StringBuffer findfrombyUsername(String userName) {
		List<TbUserApplymoney> list = tbUserApplymoneyDao.findByAcceptname(userName);
		int allform = list.size();
		int wsh=0;
		int tg=0;
		int bh=0;
		//2是未通过 3是通过
		for (TbUserApplymoney app:list) {
            if(app.getStatus().equals(1)){
                wsh++;
            }else if(app.getStatus().equals(2)){
                bh++;
            }else{
                tg++;
            }
        }
		StringBuffer sb=new StringBuffer();
		if(allform>0) {
            sb.append("你一共申请报销的单数有:"+allform+"条     ");
            sb.append("\r\n");
            sb.append("审核通过的单数有:"+tg+"条         ");
            sb.append("\r\n");
            sb.append("审核未通过的单数有:"+bh+"条          ");
            sb.append("\r\n");
            sb.append("待审核的单数有:"+wsh+"条        ");
        }else{
            sb.append("你当前没有任何申请报销~~~");
        }
		return sb;
	}


	public void tjbusinesskey(SimpleSpecificationBuilder<TbUserApplymoney> builder, List<String> businesskylist) {
		if(businesskylist.size()==1){
			String buskey = businesskylist.get(0);
			builder.add("formkey", SpecificationOperator.Operator.eq.name(), buskey);
		}else{
			for (int i = 0; i < businesskylist.size(); i++) {
				String buskey = businesskylist.get(i);
			/*	if (i == businesskylist.size() - 1) {
					builder.addOr("formkey", SpecificationOperator.Operator.eq.name(), buskey);
					break;
				}*/
				builder.addOr("formkey", SpecificationOperator.Operator.eq.name(), buskey);
			}
		}
	}

	@Override
	public void nopass(String id) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(id).singleResult();
		List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey(id).list();
		Task task = taskList.get(0);
		String taskId = task.getId();
		HuituiUtils utils=new HuituiUtils();
		try {
			utils.commitProcess(taskId,null,"usertask1",processEngine);
			List<TbUserApplymoney> form = tbUserApplymoneyDao.findByFormkey(id);
			TbUserApplymoney xiugai = form.get(0);
			xiugai.setStatus(2);
			tbUserApplymoneyDao.save(xiugai);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<TbUserApplymoney> findByFormkey(String formkey) {
		return tbUserApplymoneyDao.findByFormkey(formkey);
	}

	public void editformPass(Integer id,String businesskey,TbUserApplymoney tbUserApplymoney) {

		TbUserApplymoney ta = tbUserApplymoneyDao.findOne(id);
		ta.setStatus(1);
		ta.setCosttype(tbUserApplymoney.getCosttype());
		ta.setDescription(tbUserApplymoney.getDescription());
		ta.setUpdateTime(new Date());
		ta.setSum(tbUserApplymoney.getSum());
		ta.setFormname(tbUserApplymoney.getFormname());
		tbUserApplymoneyDao.save(ta);
		//先更新表数据，设置流程变量金额，其他变量不懂

		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businesskey).singleResult();
		List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey(businesskey).list();
		Task task = taskList.get(0);
		String taskId = task.getId();
		Map<String, Object> var=new HashMap<>();
		var.put("money",tbUserApplymoney.getSum());
		processEngine.getTaskService().complete(taskId,var);


	}

	@Autowired
	ApplyMoneyMapper applyMoneyMapper;
	@Override
	public List<String> findApplyBydatenum(int sj) {
		List<PicResult> listapply = applyMoneyMapper.findApplybyDate(sj);
		List<String > listre=new ArrayList<>();
		for (PicResult pic: listapply) {
			listre.add(pic.getNum());
		}
		return listre;
	}

	@Override
	public List<String> findApplyBydatename(int sj) {
		List<PicResult> listapply = applyMoneyMapper.findApplybyDate(sj);
		List<String > listre=new ArrayList<>();
		for (PicResult pic: listapply) {
			listre.add(pic.getName());
		}
		return listre;
	}
}
