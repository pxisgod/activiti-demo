package com.px.manager;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class ProcessInitiator implements InitializingBean {

	private RepositoryService repositoryService;

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		boolean createNew = true;
		if (!createNew) {
			ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
			pdq.processDefinitionKey("myProcess");
			List<ProcessDefinition> pdList = pdq.orderByProcessDefinitionVersion().desc().list();
			if (!pdList.isEmpty()) {
				ProcessManager.setProcessDefinitionId(pdList.get(0).getId());
				return;
			}
		}
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("qjlc.bpmn");
		deploymentBuilder.addClasspathResource("qjlc.png");
		//发布流程
		Deployment deployment = deploymentBuilder.deploy();
		ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
		pdq.deploymentId(deployment.getId());
		ProcessDefinition pd = pdq.list().get(0);
		ProcessManager.setProcessDefinitionId(pd.getId());
	}

}
