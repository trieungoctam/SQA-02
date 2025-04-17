package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.VerifyEmail;

public interface VerifyEmailService {

	void saveVerifyEmail(VerifyEmail verifyEmail);

	VerifyEmail findByEmail(String email);

	Boolean isOtpExpiredTime(VerifyEmail verifyEmail);

	Boolean isOtpMatched(String otp, VerifyEmail verifyEmail);
}
