package com.spring.privateClinicManage.filter;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.JwtService;
import com.spring.privateClinicManage.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class JwtAuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	private final static String TOKEN_HEADER = "authorization";
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserService userService;

	@Override
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String authToken = httpRequest.getHeader(TOKEN_HEADER);
		try {
			if (jwtService.validateTokenLogin(authToken)) {
				String email = jwtService.getEmailFromToken(authToken);
				User user = userService.findByEmail(email);
				if (user != null && user.getActive() == true) {
					boolean enabled = true;
					boolean accountNonExpired = true;
					boolean credentialsNonExpired = true;
					boolean accountNonLocked = true;

					Set<GrantedAuthority> authorities = new HashSet<>();
					authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

					UserDetails userDetail = new org.springframework.security.core.userdetails.User(
							email, user.getPassword(), enabled, accountNonExpired,
							credentialsNonExpired, accountNonLocked, authorities);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetail,
							null, userDetail.getAuthorities());
					authentication
							.setDetails(
									new WebAuthenticationDetailsSource().buildDetails(httpRequest));

//					SecurityContextHolder.getContext().setAuthentication(authentication); // should not do this

					SecurityContext context = SecurityContextHolder.createEmptyContext();
					context.setAuthentication(authentication);
					SecurityContextHolder.setContext(context);

				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}

}
