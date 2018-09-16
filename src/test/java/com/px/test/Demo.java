package com.px.test;

import java.util.List;

import org.activiti.engine.EngineServices;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试  
//@ContextConfiguration(locations={"classpath:activiti.cfg.xml"}) //加载配置文件  
public class Demo {
	// @Autowired
	// private ProcessEngineFactoryBean factoryBean;

	// ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	@Test
	public void test1() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// 使用配置对象创建一个流程引擎对象，并且在创建过程中可以自动建表
		ProcessEngine processEngine = conf.buildProcessEngine();
	}

	@Test
	public void test2() {

		// ProcessEngine processEngine
		// =factoryBean.getProcessEngineConfiguration().buildProcessEngine();

		/*ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// 使用配置对象创建一个流程引擎对象，并且在创建过程中可以自动建表
		ProcessEngine processEngine = conf.buildProcessEngine();*/

		
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		ProcessEngine processEngine = conf.buildProcessEngine();
		/*ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();*/
		// 创建一个部署构建器对象，用于加载流程定义文件(bpmn文件和png文件)
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment();
		deploymentBuilder.addClasspathResource("qjlc.bpmn");
		deploymentBuilder.addClasspathResource("qjlc.png");
		// 部署，并返回一个部署对象(其实Deployment是一个接口)
		Deployment deployment = deploymentBuilder.deploy();
		System.out.println(deployment.getId());
	}

	@Test
	public void test3() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// 使用配置对象创建一个流程引擎对象，并且在创建过程中可以自动建表
		ProcessEngine processEngine = conf.buildProcessEngine();
		// 流程定义查询对象，用于查询流程定义表（act_re_procdef）
		ProcessDefinitionQuery query = processEngine.getRepositoryService().createProcessDefinitionQuery();
		// 使用key过滤
		query.processDefinitionKey("myProcess");
		// 以下查询的是所有的流程定义
		List<ProcessDefinition> list = query.list();
		for (ProcessDefinition pd : list) {
			System.out.println(pd.getId() + "    " + pd.getName() + "    " + pd.getVersion());
		}
	}

	@Test
	public void test4() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// 使用配置对象创建一个流程引擎对象，并且在创建过程中可以自动建表
		ProcessEngine processEngine = conf.buildProcessEngine();
		String processDefinitionId = "myProcess:5:12504"; // 流程定义id
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceById(processDefinitionId); // 根据请假流程定义来具体地请一次假，即启动流程实例
		System.out.println(processInstance.getId());
	}

	@Test
	public void test5() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// 使用配置对象创建一个流程引擎对象，并且在创建过程中可以自动建表
		ProcessEngine processEngine = conf.buildProcessEngine();
		// 任务查询对象，操作的是任务表(act_ru_task)
		TaskQuery query = processEngine.getTaskService().createTaskQuery();
		// 根据任务的办理人过滤
		query.taskAssignee("张沟"); // 只查询张三的任务，其他人的任务不查
		List<Task> list = query.list();
		for (Task task : list) {
			System.out.println(task.getId() + "\t" + task.getName() + "\t" + task.getAssignee());
		}
	}

	@Test
	public void test6() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// 使用配置对象创建一个流程引擎对象，并且在创建过程中可以自动建表
		ProcessEngine processEngine = conf.buildProcessEngine();
		String taskId = "20004"; // 任务的id
		processEngine.getTaskService().complete(taskId);
	}
	@Test
    public void test7() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// 使用配置对象创建一个流程引擎对象，并且在创建过程中可以自动建表
		ProcessEngine processEngine = conf.buildProcessEngine();
        // 流程实例查询对象，操作的是流程实例表(act_ru_execution)
        ProcessInstanceQuery query = processEngine.getRuntimeService().createProcessInstanceQuery();
        List<ProcessInstance> list = query.list();
        for (ProcessInstance processInstance : list) {
            System.out.println(processInstance.getId());
        }
    }
	
	@Test
    public void test8() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// 使用配置对象创建一个流程引擎对象，并且在创建过程中可以自动建表
		ProcessEngine processEngine = conf.buildProcessEngine();
        String processInstanceId = "10001"; // 流程实例id
        String deleteReason = "不请假了"; // 删除原因，任君写
        processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, deleteReason);
    }
	
	@Test
    public void test9() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// 使用配置对象创建一个流程引擎对象，并且在创建过程中可以自动建表
		ProcessEngine processEngine = conf.buildProcessEngine();
        
    }
	
	
}



