package com.px.vo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class TaskInstVo {

	private String startUserName;
	private Date startTime;
	private String taskId;
	public String getStartUserName() {
		return startUserName;
	}
	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}
	
	@JSONField (format="yyyy-MM-dd")  
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}
