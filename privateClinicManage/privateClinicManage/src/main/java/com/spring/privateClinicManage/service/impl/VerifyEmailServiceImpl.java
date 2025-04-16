package com.spring.privateClinicManage.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.VerifyEmail;
import com.spring.privateClinicManage.repository.VerifyEmailRepository;
import com.spring.privateClinicManage.service.VerifyEmailService;

import jakarta.transaction.Transactional;

@Service
public class VerifyEmailServiceImpl implements VerifyEmailService {

	@Autowired
	private VerifyEmailRepository verifyEmailRepository;
	@Autowired
	private PasswordEncoder encoder;

	@Override
	@Transactional
	public void saveVerifyEmail(VerifyEmail verifyEmail) {
		verifyEmail.setOtp(encoder.encode(verifyEmail.getOtp()));
		verifyEmailRepository.save(verifyEmail);
	}

	@Override
	public VerifyEmail findByEmail(String email) {
		return verifyEmailRepository.findByEmail(email);
	}

	@Override
	public Boolean isOtpExpiredTime(VerifyEmail verifyEmail) {
		return verifyEmail.getExpriedTime().isBefore(LocalDateTime.now());
	}

	@Override
	public Boolean isOtpMatched(String otp, VerifyEmail verifyEmail) {
		return encoder.matches(otp, verifyEmail.getOtp());
	}

}
