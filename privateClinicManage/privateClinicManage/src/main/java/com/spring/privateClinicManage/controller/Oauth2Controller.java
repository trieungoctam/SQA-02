package com.spring.privateClinicManage.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.oauth2.Dto.CustomOAuth2User;
import com.spring.privateClinicManage.service.JwtService;
import com.spring.privateClinicManage.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class Oauth2Controller {

	@Autowired
	private UserService userService;
	@Autowired
	private JwtService jwtService;

	@GetMapping("/oauth2")
	public String hello() {
		return "admin/hello";
	}

	@GetMapping("/oauth2/create-token/")
	public ResponseEntity<Object> getOauth2Token(OAuth2AuthenticationToken authentication,
			HttpServletResponse response) throws IOException {

		if (authentication == null)
			return new ResponseEntity<Object>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);

		CustomOAuth2User oauthUser = (CustomOAuth2User) authentication
				.getPrincipal();

		Map<String, Object> googleUser = oauthUser.getAttributes();

		String email = (String) googleUser.get("email");

		User existUser = userService.findByEmail(email);

		if (existUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.UNAUTHORIZED);

		if (!existUser.getActive())
			return new ResponseEntity<>("Tài khoản đã bị khóa", HttpStatus.UNAUTHORIZED);

		String token = jwtService.generateTokenLogin(email);

		response.sendRedirect("http://localhost:3000?token=" + token);

		return new ResponseEntity<Object>(token, HttpStatus.OK);
	}
}
