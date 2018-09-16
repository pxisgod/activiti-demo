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

//@RunWith(SpringJUnit4ClassRunner.class) //ʹ��junit4���в���  
//@ContextConfiguration(locations={"classpath:activiti.cfg.xml"}) //���������ļ�  
public class Demo {
	// @Autowired
	// private ProcessEngineFactoryBean factoryBean;

	// ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	@Test
	public void test1() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// ʹ�����ö��󴴽�һ������������󣬲����ڴ��������п����Զ�����
		ProcessEngine processEngine = conf.buildProcessEngine();
	}

	@Test
	public void test2() {

		// ProcessEngine processEngine
		// =factoryBean.getProcessEngineConfiguration().buildProcessEngine();

		/*ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// ʹ�����ö��󴴽�һ������������󣬲����ڴ��������п����Զ�����
		ProcessEngine processEngine = conf.buildProcessEngine();*/

		
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		ProcessEngine processEngine = conf.buildProcessEngine();
		/*ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();*/
		// ����һ�����𹹽����������ڼ������̶����ļ�(bpmn�ļ���png�ļ�)
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment();
		deploymentBuilder.addClasspathResource("qjlc.bpmn");
		deploymentBuilder.addClasspathResource("qjlc.png");
		// ���𣬲�����һ���������(��ʵDeployment��һ���ӿ�)
		Deployment deployment = deploymentBuilder.deploy();
		System.out.println(deployment.getId());
	}

	@Test
	public void test3() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// ʹ�����ö��󴴽�һ������������󣬲����ڴ��������п����Զ�����
		ProcessEngine processEngine = conf.buildProcessEngine();
		// ���̶����ѯ�������ڲ�ѯ���̶����act_re_procdef��
		ProcessDefinitionQuery query = processEngine.getRepositoryService().createProcessDefinitionQuery();
		// ʹ��key����
		query.processDefinitionKey("myProcess");
		// ���²�ѯ�������е����̶���
		List<ProcessDefinition> list = query.list();
		for (ProcessDefinition pd : list) {
			System.out.println(pd.getId() + "    " + pd.getName() + "    " + pd.getVersion());
		}
	}

	@Test
	public void test4() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// ʹ�����ö��󴴽�һ������������󣬲����ڴ��������п����Զ�����
		ProcessEngine processEngine = conf.buildProcessEngine();
		String processDefinitionId = "myProcess:5:12504"; // ���̶���id
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceById(processDefinitionId); // ����������̶������������һ�μ٣�����������ʵ��
		System.out.println(processInstance.getId());
	}

	@Test
	public void test5() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// ʹ�����ö��󴴽�һ������������󣬲����ڴ��������п����Զ�����
		ProcessEngine processEngine = conf.buildProcessEngine();
		// �����ѯ���󣬲������������(act_ru_task)
		TaskQuery query = processEngine.getTaskService().createTaskQuery();
		// ��������İ����˹���
		query.taskAssignee("�Ź�"); // ֻ��ѯ���������������˵����񲻲�
		List<Task> list = query.list();
		for (Task task : list) {
			System.out.println(task.getId() + "\t" + task.getName() + "\t" + task.getAssignee());
		}
	}

	@Test
	public void test6() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// ʹ�����ö��󴴽�һ������������󣬲����ڴ��������п����Զ�����
		ProcessEngine processEngine = conf.buildProcessEngine();
		String taskId = "20004"; // �����id
		processEngine.getTaskService().complete(taskId);
	}
	@Test
    public void test7() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// ʹ�����ö��󴴽�һ������������󣬲����ڴ��������п����Զ�����
		ProcessEngine processEngine = conf.buildProcessEngine();
        // ����ʵ����ѯ���󣬲�����������ʵ����(act_ru_execution)
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
		// ʹ�����ö��󴴽�һ������������󣬲����ڴ��������п����Զ�����
		ProcessEngine processEngine = conf.buildProcessEngine();
        String processInstanceId = "10001"; // ����ʵ��id
        String deleteReason = "�������"; // ɾ��ԭ���ξ�д
        processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, deleteReason);
    }
	
	@Test
    public void test9() {
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
		// ʹ�����ö��󴴽�һ������������󣬲����ڴ��������п����Զ�����
		ProcessEngine processEngine = conf.buildProcessEngine();
        
    }
	
	
}



