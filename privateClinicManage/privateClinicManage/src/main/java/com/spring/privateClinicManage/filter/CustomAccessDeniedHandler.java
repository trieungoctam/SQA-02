package com.spring.privateClinicManage.filter;

import java.io.IOException;

import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest hsr, HttpServletResponse response,
			org.springframework.security.access.AccessDeniedException ade)
			throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write("Access Denied!");
	}

}
