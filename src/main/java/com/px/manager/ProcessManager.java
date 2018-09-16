package com.px.manager;


public class ProcessManager{
	
	private static String processDefinitionId;

	public static String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public static void setProcessDefinitionId(String processDefinitionId) {
		ProcessManager.processDefinitionId = processDefinitionId;
	}
	
	

}
