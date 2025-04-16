package com.spring.privateClinicManage.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.spring.privateClinicManage.filter.CustomAccessDeniedHandler;
import com.spring.privateClinicManage.filter.JwtAuthenticationTokenFilter;
import com.spring.privateClinicManage.filter.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@Order(1)
public class JwtSecurityConfig {

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
		JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
		return jwtAuthenticationTokenFilter;
	}

	@Bean
	public RestAuthenticationEntryPoint restServicesEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

	@Bean
	public CustomAccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public SecurityFilterChain jwtSecurityfilterChain(HttpSecurity http) throws Exception {

		http.securityMatcher("/api/**").authorizeHttpRequests(auth -> auth

				.requestMatchers(HttpMethod.GET,
						"/api/users/getAllStatusIsApproved/",
//						"/api/qr/barcodes/zxing/qrcode/",
						"/api/payment/momo/return/",
						"/api/payment/vnpay/return/",
						"/api/v1/coze/**",
						"/api/v1/hugging-face/**")
				.permitAll()

				.requestMatchers(HttpMethod.POST,
						"/api/users/login/",
						"/api/users/register/",
						"/api/users/verify-email/",
						"/api/pdf/generate/",
						"/api/users/take-order-from-qrCode/",
						"/api/v1/hugging-face/**",
						"/api/v1/hugging-face/completion-stream")
				.permitAll()

				.requestMatchers(HttpMethod.GET,
						"/api/benhnhan/get-mrl-and-me-user-history/",
						"/api/benhnhan/receive-voucher/{urlId}/")
				.hasRole("BENHNHAN")

				.requestMatchers(HttpMethod.POST,
						"/api/benhnhan/register-schedule/",
						"/api/benhnhan/user-register-schedule-list/",
						"/api/benhnhan/apply-voucher/",
						"/api/payment/**",
						"/api/benhnhan/get-payment-history-by-name/")
				.hasRole("BENHNHAN")

				.requestMatchers(HttpMethod.PATCH,
						"/api/benhnhan/cancel-register-schedule/{registerScheduleId}/")
				.hasRole("BENHNHAN")

				.requestMatchers(HttpMethod.GET,
						"/api/yta/all-register-schedule/")
				.hasRole("YTA")

				.requestMatchers(HttpMethod.POST,
						"/api/yta/get-users-schedule-status/",
						"/api/yta/auto-confirm-registers/",
						"/api/yta/direct-register/",
						"/api/yta/cash-payment/")
				.hasRole("YTA")

				.requestMatchers(HttpMethod.GET,
						"/api/bacsi/get-all-processing-user-today/",
						"/api/bacsi/get-all-medicine-group/",
						"/api/bacsi/get-all-medicine-by-group/{medicineGroupId}/",
						"/api/bacsi/get-medicine-by-id/{medicineId}/",
						"/api/bacsi/get-all-medicines/")
				.hasRole("BACSI")

				.requestMatchers(HttpMethod.POST,
						"/api/bacsi/submit-medical-examination/")
				.hasRole("BACSI")

				.requestMatchers("/api/anyrole/blogs/",
						"/api/anyrole/blogs/{blogId}/get-comment-blog/",
						"/api/anyrole/blogs/{blogId}/count-likes/")
				.permitAll()
				.requestMatchers("/api/anyrole/**",
						"/api/users/current-user/", "/api/yta/get-all-users/")
				.hasAnyRole("BENHNHAN", "BACSI", "YTA", "TUVAN")

				.requestMatchers("/api/anyrole/get-history-user-register/",
						"/api/bacsi/get-prescriptionItems-by-medicalExam-id/{medicalExamId}/")
				.hasAnyRole("BENHNHAN", "BACSI")

				.requestMatchers("/api/anyrole/get-medical-exam-by-mrlId/{mrlId}/")
				.hasAnyRole("BENHNHAN", "YTA")

				.anyRequest().authenticated())
				.httpBasic(httpbc -> httpbc
						.authenticationEntryPoint(restServicesEntryPoint()))
				.sessionManagement(smc -> smc
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exp -> exp.authenticationEntryPoint(restServicesEntryPoint())
						.accessDeniedHandler(customAccessDeniedHandler()))
				.addFilterBefore(
						jwtAuthenticationTokenFilter(),
						UsernamePasswordAuthenticationFilter.class)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(List.of("*"));
		configuration.addAllowedHeader("*");
		configuration.addExposedHeader("*");

		configuration.addAllowedOriginPattern("*");
		configuration.setAllowCredentials(false);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


}
