package com.px.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = { "classpath:activiti-context.xml" })
public class Demo2 {

	@Autowired
	private IdentityService identityService;

	@Autowired
	private ProcessEngine processEngine;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ManagementService managementService;

	@Autowired
	// @Rule
	private ActivitiRule activitiRule;

	@Test
	public void test1() {

		boolean createNew = true;
		if (!createNew) {
			ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
			pdq.processDefinitionKey("myProcess");
			List<ProcessDefinition> pdList = pdq.orderByProcessDefinitionVersion().desc().list();
			if (!pdList.isEmpty()) {
				System.out.println(pdList.get(0).getId());
				return;
			}
		}

		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("qjlc.bpmn");
		deploymentBuilder.addClasspathResource("qjlc.png");

		// ���𣬲�����һ���������(��ʵDeployment��һ���ӿ�)
		Deployment deployment = deploymentBuilder.deploy();
		ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
		pdq.deploymentId(deployment.getId());
		ProcessDefinition pd = pdq.list().get(0);
		System.out.println(pd.getId());
	}

	@Test
	public void test2() {
		String processDefinitionId = "myProcess:18:107504"; 
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userId1", "3");
		identityService.setAuthenticatedUserId("3");
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, variables);
		System.out.println(processInstance.getId());
	}

	@Test
	public void test5() {

		// if(user="3"){
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userId2", "2");
		String taskId = "110006"; 
		taskService.complete(taskId, variables);
		// }
	}

	@Test
	public void test7() {
		TaskQuery tq = taskService.createTaskQuery();
		tq.taskAssignee("2");
		List<Task> taskList = tq.list();
		for (Task task : taskList) {
			System.out.println(task.getName() + "  " + task.getId());
		}
	}

	@Test
	public void test3() {
		// if(user=="2")
		String taskId = "117503"; 
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userId3", "1");
		taskService.complete(taskId, variables);
		// }
	}

	@Test
	public void test8() {

		TaskQuery tq = taskService.createTaskQuery();
		tq.taskAssignee("1");
		List<Task> taskList = tq.list();
		for (Task task : taskList) {
			System.out.println(task.getName() + "  " + task.getId());
		}

	}

	@Test
	public void test9() {
		// if(user=="1"){
		String taskId = "122503"; 
		taskService.complete(taskId);
		// }
	}

	@Test
	public void test10() { 
		String processInstanceId = "10001"; 
		String deleteReason = "想请假,做梦呢"; 
		runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
	}

	@Test
	public void test6() {
		HistoricProcessInstanceQuery hq = historyService.createHistoricProcessInstanceQuery();
		hq.involvedUser("3");
		hq.processDefinitionId("myProcess:18:107504");
		List<HistoricProcessInstance> hiList = hq.list();
		for (HistoricProcessInstance hi : hiList) {
			if (hi.getEndActivityId() != null || hi.getDeleteReason() != null) {
				System.out.println(hi.getId() + " completed");
			} else {
				System.out.println(hi.getId() + " not completed");
			}
			HistoricTaskInstanceQuery htq = historyService.createHistoricTaskInstanceQuery();
			htq.processDefinitionId(hi.getProcessDefinitionId());
			List<HistoricTaskInstance> pdList = htq.list();
			for (HistoricTaskInstance hti : pdList) {
				System.out
						.println(hti.getName() + " " + (hti.getDeleteReason() == null ? "没有完成" : hti.getDeleteReason()));
			}
		}
	}

	@Test
	@org.activiti.engine.test.Deployment
	public void test4() {
		runtimeService.startProcessInstanceByKey("helloProcess");
		// Task task = taskService.createTaskQuery().singleResult();
		// assertEquals("print", task.getName());
		// taskService.complete(task.getId());
		// assertEquals(0, runtimeService.createProcessInstanceQuery().count());

	}

}
