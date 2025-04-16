package com.spring.privateClinicManage.service;


import java.io.UnsupportedEncodingException;

import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.StatusIsApproved;

import jakarta.mail.MessagingException;

public interface MailSenderService {
	void sendOtpEmail(String email)
			throws MessagingException, UnsupportedEncodingException;

	void sendStatusRegisterEmail(MedicalRegistryList mrl, String content,
			StatusIsApproved statusIsApproved)
			throws MessagingException, UnsupportedEncodingException;

}
