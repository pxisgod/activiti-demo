package com.px.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.px.entity.Emp;
import com.px.entity.EmpExample;
import com.px.manager.ProcessManager;
import com.px.mapper.EmpMapper;
import com.px.vo.ProcessDefinitionVo;
import com.px.vo.ProcessInstVo;
import com.px.vo.ProcessVo;
import com.px.vo.ResultCode;
import com.px.vo.TaskInstVo;
import com.px.vo.TaskVo;

@RestController
@RequestMapping(path = { "/activiti" }, method = { RequestMethod.POST })
public class ActivitiController {

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
	private EmpMapper empMapper;

	@RequestMapping(path = { "/start" })
	public ResultCode startProcess(HttpServletRequest req) {
		Emp user = (Emp) req.getSession().getAttribute("user");
		String pid = ProcessManager.getProcessDefinitionId();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userId1", user.getId()); // 设置分配任务的人
		identityService.setAuthenticatedUserId(user.getId().toString());// 设置发起人
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(pid, variables);
		System.out.println(processInstance.getId());
		return new ResultCode();
	}

	@RequestMapping(path = { "/doTask" })
	public ResultCode doTask(HttpServletRequest req, String taskId) {
		Emp user = (Emp) req.getSession().getAttribute("user");
		String pid = ProcessManager.getProcessDefinitionId();
		TaskQuery tq = taskService.createTaskQuery();
		tq.taskAssignee(user.getId().toString());
		tq.taskId(taskId);
		System.out.println(user.getId() + " " + taskId);
		List<Task> taskList = tq.list();
		ResultCode result = new ResultCode();
		if (!taskList.isEmpty()) {
			Task task = taskList.get(0);
			String ppid = task.getProcessInstanceId();
			Emp submitEmp = getSubmitEmp(ppid);
			System.out.println("发起人id" + submitEmp);
			if (task.getTaskDefinitionKey().equals("usertask1")) {
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("userId2", submitEmp.getSmallboss() == null ? user.getId() : submitEmp.getSmallboss());
				taskService.complete(taskId, variables);
			}
			if (task.getTaskDefinitionKey().equals("usertask2")) {
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("userId3", submitEmp.getBigboss() == null ? user.getId() : submitEmp.getBigboss());
				taskService.complete(taskId, variables);
			}
			if (task.getTaskDefinitionKey().equals("usertask3")) {
				taskService.complete(taskId);
			}
		} else {
			System.out.println("没有找到该任务");
			result.setSuccess(false);

		}
		return result;
	}

	@RequestMapping(path = { "/cancelTask" })
	public ResultCode cancelProcess(HttpServletRequest req, String taskId) {
		Emp user = (Emp) req.getSession().getAttribute("user");
		String pid = ProcessManager.getProcessDefinitionId();
		TaskQuery tq = taskService.createTaskQuery();
		tq.taskAssignee(user.getId().toString());
		tq.taskId(taskId);
		List<Task> taskList = tq.list();
		ResultCode result = new ResultCode();
		if (!taskList.isEmpty()) {
			Task task = taskList.get(0);
			String ppid = task.getProcessInstanceId();
			String deleteReason = "想请假，没门"; // 设置失败原因
			runtimeService.deleteProcessInstance(ppid, deleteReason);
		}else{
			result.setSuccess(false);
		}
		return result;
	}

	@RequestMapping(path = { "/getAllTask" })
	public ResultCode getAllTask(HttpServletRequest req) {
		Emp user = (Emp) req.getSession().getAttribute("user");
		String pid = ProcessManager.getProcessDefinitionId();
		HistoricProcessInstanceQuery hq = historyService.createHistoricProcessInstanceQuery();
		hq.processDefinitionId(pid);
		TaskQuery tq = taskService.createTaskQuery();
		tq.taskAssignee(user.getId().toString());
		tq.processDefinitionId(pid);
		List<Task> taskList = tq.list();
		List<TaskVo> taskVoList = new ArrayList<TaskVo>();
		for (Task task : taskList) {
			hq.processInstanceId(task.getProcessInstanceId());
			TaskVo vo = new TaskVo();
			vo.setTaskId(task.getId());
			vo.setTaskName(task.getName());
			String startUserId = hq.list().get(0).getStartUserId();
			Emp emp = getByUserId(startUserId);
			vo.setStartUserName(emp.getName());
			taskVoList.add(vo);
			// System.out.println(task.getName() + " " + task.getId());

		}
		ResultCode result = new ResultCode();
		result.setResultInfo(taskVoList);
		return result;
	}

	@RequestMapping(path = { "/getAllProcess" })
	public ResultCode getAllProcess(HttpServletRequest req) {
		Emp user = (Emp) req.getSession().getAttribute("user");
		String pid = ProcessManager.getProcessDefinitionId();
		HistoricProcessInstanceQuery hq = historyService.createHistoricProcessInstanceQuery();
		hq.startedBy(user.getId().toString());// 设置发起人user
		// hq.involvedUser(user.getId().toString());//process中包含的所有user
		hq.processDefinitionId(pid);
		List<HistoricProcessInstance> hiList = hq.list();
		List<ProcessVo> processVoList = new ArrayList<ProcessVo>();
		for (HistoricProcessInstance hi : hiList) {
			ProcessVo vo = new ProcessVo();
			vo.setProcessInstId(hi.getId());
			processVoList.add(vo);
		}
		ResultCode result = new ResultCode();
		result.setResultInfo(processVoList);
		return result;
	}

	@RequestMapping(path = { "/getProcess" })
	public ResultCode getProcess(HttpServletRequest req, String processInstId) {

		Emp user = (Emp) req.getSession().getAttribute("user");
		String pid = ProcessManager.getProcessDefinitionId();
		HistoricProcessInstanceQuery hq = historyService.createHistoricProcessInstanceQuery();
		hq.startedBy(user.getId().toString());// 设置发起人user
		// hq.involvedUser(user.getId().toString());//process中包含的所有user
		hq.processInstanceId(processInstId);
		List<HistoricProcessInstance> hiList = hq.list();
		ResultCode result = new ResultCode();
		if (!hiList.isEmpty()) {
			ProcessInstVo vo = new ProcessInstVo();
			HistoricProcessInstance hi = hiList.get(0);
			if (hi.getEndActivityId() != null || hi.getDeleteReason() != null) {
				vo.setIsComplete(true);
				if (hi.getEndActivityId() == null) {
					vo.setDeleteReason(hi.getDeleteReason());
					vo.setIsCanceled(true);
				} else
					vo.setIsCanceled(false);
			} else {
				vo.setIsComplete(false);
			}
			HistoricTaskInstanceQuery htq = historyService.createHistoricTaskInstanceQuery();
			htq.processInstanceId(processInstId);
			List<HistoricTaskInstance> pdList = htq.list();
			for (HistoricTaskInstance hti : pdList) {
				ProcessDefinitionVo pdVo = new ProcessDefinitionVo();
				pdVo.setTaskKey(hti.getTaskDefinitionKey());
				pdVo.setTaskName(hti.getName());
				vo.getComplete().add(pdVo);
			}
			ProcessDefinitionVo pdVo = new ProcessDefinitionVo();
			pdVo.setTaskKey("usertask1");
			pdVo.setTaskName("提交请假申请");
			vo.getTotal().add(pdVo);
			pdVo = new ProcessDefinitionVo();
			pdVo.setTaskKey("usertask2");
			pdVo.setTaskName("项目经理审核");
			vo.getTotal().add(pdVo);
			pdVo = new ProcessDefinitionVo();
			pdVo.setTaskKey("usertask3");
			pdVo.setTaskName("部门经理审核");
			vo.getTotal().add(pdVo);
			result.setResultInfo(vo);
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	@RequestMapping(path = { "/getTask" })
	public ResultCode getTask(HttpServletRequest req, String taskId) {
		Emp user = (Emp) req.getSession().getAttribute("user");
		String pid = ProcessManager.getProcessDefinitionId();
		TaskQuery tq = taskService.createTaskQuery();
		tq.taskAssignee(user.getId().toString());
		tq.taskId(taskId);
		List<Task> taskList = tq.list();
		ResultCode result = new ResultCode();
		if (!taskList.isEmpty()) {
			Task task = taskList.get(0);
			String piId = task.getProcessInstanceId();
			HistoricProcessInstanceQuery hq = historyService.createHistoricProcessInstanceQuery();
			hq.processInstanceId(piId);
			HistoricProcessInstance hpi = hq.list().get(0);
			TaskInstVo vo = new TaskInstVo();
			vo.setStartUserName(getByUserId(hpi.getStartUserId()).getName());
			vo.setStartTime(hpi.getStartTime());
			vo.setTaskId(task.getId());
			result.setResultInfo(vo);
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	private Emp getSubmitEmp(String ppid) {
		// TODO Auto-generated method stub
		HistoricProcessInstanceQuery hq = historyService.createHistoricProcessInstanceQuery();
		hq.processInstanceId(ppid);
		Integer submitEmpId = Integer.parseInt(hq.list().get(0).getStartUserId());
		EmpExample example = new EmpExample();
		example.createCriteria().andIdEqualTo(submitEmpId);
		List<Emp> emps = empMapper.selectByExample(example);
		if (!emps.isEmpty())
			return emps.get(0);
		return null;

	}

	private Emp getByUserId(String startUserId) {
		EmpExample example = new EmpExample();
		example.createCriteria().andIdEqualTo(Integer.parseInt(startUserId));
		Emp emp = empMapper.selectByExample(example).get(0);
		return emp;
	}
}
