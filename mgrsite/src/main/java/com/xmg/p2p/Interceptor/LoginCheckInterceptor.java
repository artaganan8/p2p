package com.xmg.p2p.Interceptor;

import com.xmg.p2p.base.util.UserContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 专门用于登陆检查的拦截器
 * @author Administrator
 *
 */
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// 判断登陆逻辑
		if(UserContext.getCurrent()==null){
			response.sendRedirect("/login.html");
			return false;
		}
		return super.preHandle(request, response, handler);
	}

}
