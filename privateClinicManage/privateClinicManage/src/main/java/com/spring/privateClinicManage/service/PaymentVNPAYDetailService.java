package com.spring.privateClinicManage.service;

import java.io.UnsupportedEncodingException;

import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Voucher;

public interface PaymentVNPAYDetailService {

	String generateUrlPayment(Long amount, MedicalRegistryList mrl, Voucher voucher)
			throws UnsupportedEncodingException;
}
