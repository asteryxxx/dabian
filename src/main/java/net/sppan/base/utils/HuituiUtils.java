package net.sppan.base.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;

public class HuituiUtils {
	/**
     * 根据任务ID获取流程定义
     * 
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
	public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(  
            String taskId,ProcessEngine processEngine) throws Exception {  

		// 取得流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngine.getRepositoryService())  
                .getDeployedProcessDefinition(findTaskById(taskId,processEngine)  
                        .getProcessDefinitionId());  
        if (processDefinition == null) {  
        	throw	new Exception("流程定义未找到!");  
        }  
        return processDefinition;  
    }

	/**
	 * 根据任务ID获得任务实例
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	public TaskEntity findTaskById(String taskId ,ProcessEngine processEngine) throws Exception {
		TaskEntity task = (TaskEntity) processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new Exception("任务实例未找到!");
		}
		return task;
	}

	/**
	 * @param taskId
	 *            当前任务ID
	 * @param variables
	 *            流程变量
	 * @param activityId
	 *            流程转向执行任务节点ID 此参数为空，默认为提交操作
	 * @throws Exception
	 */
	public void commitProcess(String taskId, Map<String, Object> variables, String activityId,ProcessEngine processEngine) throws Exception {
		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		// 跳转节点为空，默认提交操作
		if (activityId == null) {
			processEngine.getTaskService().complete(taskId, variables);
		} else {// 流程转向操作
			turnTransition(taskId, activityId, variables,processEngine);
		}
	}

	/**
	 * 流程转向操作
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            目标节点任务ID
	 * @param variables
	 *            流程变量
	 * @throws Exception
	 */
	public void turnTransition(String taskId, String activityId, Map<String, Object> variables,ProcessEngine processEngine) throws Exception {
		// 当前节点
		ActivityImpl currActivity = findActivitiImpl(taskId, null,processEngine);
		// 清空当前流向
		List oriPvmTransitionList = clearTransition(currActivity);
		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = findActivitiImpl(taskId, activityId,processEngine);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);
		// 执行转向任务
		processEngine.getTaskService().complete(taskId, variables);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);
		// 还原以前流向
		restoreTransition(currActivity, oriPvmTransitionList);
	}

	/**
	 * 清空指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
	/**
	 * 清空指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
	public List clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List oriPvmTransitionList = new ArrayList();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (int i = 0; i < pvmTransitionList.size(); i++) {
			PvmTransition pvmTransition = (PvmTransition) pvmTransitionList.get(i);
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();
		return oriPvmTransitionList;
	}

	/**
	 * 还原指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
	public void restoreTransition(ActivityImpl activityImpl, List oriPvmTransitionList) {
		// 清空现有流向
		List pvmTransitionList = activityImpl.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向

		for (int i = 0; i < oriPvmTransitionList.size(); i++) {
			PvmTransition pvmTransition = (PvmTransition) oriPvmTransitionList.get(i);
			pvmTransitionList.add(pvmTransition);
		}
	}
	
	/**
	 * 根据任务ID和节点ID获取活动节点
	 * 
	 * @param taskId
	 *            任务ID
	 * @param activityId
	 *            活动节点ID 如果为null或""，则默认查询当前活动节点 如果为"end"，则查询结束节点
	 * 
	 * @return
	 * @throws Exception
	 */
	private ActivityImpl findActivitiImpl(String taskId, String activityId,ProcessEngine processEngine) throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId,processEngine);
		// 获取当前活动节点ID
		if (activityId == null) {
			activityId = findTaskById(taskId,processEngine).getTaskDefinitionKey();
		}
		// 根据流程定义，获取该流程实例的结束节点
		if (activityId.equals("zongjinglishenhe")) {
			for (ActivityImpl activityImpl : processDefinition.getActivities()) {
				List pvmTransitionList = activityImpl.getOutgoingTransitions();
				if (pvmTransitionList.isEmpty()) {
					return activityImpl;
				}
			}
		}
		// 根据节点ID，获取对应的活动节点
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);
		return activityImpl;
	}
}
