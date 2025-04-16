package com.spring.privateClinicManage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.itextpdf.io.exceptions.IOException;
import com.spring.privateClinicManage.oauth2.Dto.CustomOAuth2User;
import com.spring.privateClinicManage.oauth2.service.CustomOAuth2UserService;
import com.spring.privateClinicManage.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;

@Configuration
@EnableWebSecurity
@Order(2)
public class SpringSecurityConfig {

	@Autowired
	private CustomOAuth2UserService oauthUserService;
	@Autowired
	private UserService userService;
	@Autowired
	private Environment env;

	@Bean
	public SecurityFilterChain springFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login/**", "/oauth2/**", "/error", "/public/resources/**",
						"/qr/**", "/ws/**")
				.permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/oauth2/current-user/").authenticated()
				.anyRequest().authenticated())
				.formLogin((form) -> form.loginPage("/login").loginProcessingUrl("/login")
						.usernameParameter("email").passwordParameter("password")
						.defaultSuccessUrl("/admin", true).permitAll())
				.oauth2Login((o) -> o.loginPage("/oauth2")
						.defaultSuccessUrl("/oauth2/current-user/", true)
						.userInfoEndpoint(userInfo -> userInfo
								.userService(oauthUserService))
						.successHandler(new AuthenticationSuccessHandler() {

							@Override
							public void onAuthenticationSuccess(HttpServletRequest request,
									HttpServletResponse response,
									Authentication authentication)
									throws IOException, ServletException, java.io.IOException {

								CustomOAuth2User oauthUser = (CustomOAuth2User) authentication
										.getPrincipal();

								userService.processOAuthPostLogin(oauthUser);

								response.sendRedirect("http://localhost:8888/oauth2/create-token/");
							}
						}))

				.logout((logout) -> logout.permitAll())
				.cors(cors -> cors.disable())
				.csrf(csrf -> csrf.disable());

		return http.build();
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		ClientRegistration github = githubClientRegistration();
		ClientRegistration google = googleClientRegistration();
		return new InMemoryClientRegistrationRepository(github, google);
	}

	private ClientRegistration githubClientRegistration() {
		return CommonOAuth2Provider.GITHUB.getBuilder("github").clientId(env.getProperty("GITHUB_CLIENT_ID"))
				.clientSecret(env.getProperty("GITHUB_KEY")).build();
	}

	private ClientRegistration googleClientRegistration() {
		return CommonOAuth2Provider.GOOGLE.getBuilder("google").scope("email", "profile")
				.clientId(env.getProperty("GOOGLE_CLIENT_ID"))
				.clientSecret(env.getProperty("GOOGLE_KEY")).build();
	}

}