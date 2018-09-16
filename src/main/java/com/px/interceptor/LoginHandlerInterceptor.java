package com.px.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.px.entity.Emp;
import com.px.vo.ResultCode;

public class LoginHandlerInterceptor implements HandlerInterceptor {

	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		Emp user = (Emp) req.getSession().getAttribute("user");
		if (user == null) {
			if (isAjax(req)) {
				ResultCode result = new ResultCode();
				result.setSuccess(false);
				sendJsonMessage(resp, result);
			}else{
				resp.sendRedirect(req.getContextPath()+"/login.html");
			}
			return false;
		}
		return true;
	}

	

    /**
     * 判断是否是ajax请求
     * @param request
     * @return
     */
	private boolean isAjax(HttpServletRequest request) {
		String header = request.getHeader("x-requested-with");
		if (null != header && "XMLHttpRequest".endsWith(header)) {
			return true;
		}
		return false;
	}

	private static void sendJsonMessage(HttpServletResponse response, Object obj) throws Exception {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(JSONObject.toJSONString(obj));
		writer.close();
		response.flushBuffer();
	}

}
