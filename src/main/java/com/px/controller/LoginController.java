package com.px.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.px.entity.Emp;
import com.px.entity.EmpExample;
import com.px.mapper.EmpMapper;
import com.px.vo.ResultCode;

@RestController
@RequestMapping(path={"/login"},method={RequestMethod.POST})
public class LoginController {
	
	@Autowired
	private EmpMapper empMapper;
	
	@RequestMapping(path={"/login"})
	public ResultCode login(HttpServletRequest request, Integer id,String password){
		Emp user=new Emp();
		EmpExample example=new EmpExample();
		example.createCriteria().andIdEqualTo(id);
		example.createCriteria().andPasswordEqualTo(password);
		List<Emp> emps=empMapper.selectByExample(example) ;
		ResultCode result=new ResultCode();
		if(!emps.isEmpty()){
		  request.getSession().setAttribute("user",emps.get(0));
		   return result;
		}else{
			result.setSuccess(false);
			return result;
		}
	}
	
	@RequestMapping(path={"/logout"})
	public ResultCode logout(HttpServletRequest request){
		request.getSession().removeAttribute("user");
		return new ResultCode();
	}

}
