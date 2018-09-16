package com.px.vo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProcessInstVo {

	
	private String startUserName;
	private List<ProcessDefinitionVo> total=new ArrayList<ProcessDefinitionVo>();
	private List<ProcessDefinitionVo> complete=new LinkedList<ProcessDefinitionVo>();
	private Boolean isComplete;
	private Boolean isCanceled;
	private String deleteReason;
	public String getStartUserName() {
		return startUserName;
	}
	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}
	public List<ProcessDefinitionVo> getTotal() {
		return total;
	}
	public void setTotal(List<ProcessDefinitionVo> total) {
		this.total = total;
	}
	public List<ProcessDefinitionVo> getComplete() {
		return complete;
	}
	public void setComplete(List<ProcessDefinitionVo> complete) {
		this.complete = complete;
	}
	public Boolean getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}
	public Boolean getIsCanceled() {
		return isCanceled;
	}
	public void setIsCanceled(Boolean isCanceled) {
		this.isCanceled = isCanceled;
	}
	public String getDeleteReason() {
		return deleteReason;
	}
	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}
	
}
