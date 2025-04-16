package com.spring.privateClinicManage.service;

import java.util.Map;

import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Voucher;

public interface PaymentMOMODetailService {
	Map<String, Object> generateMOMOUrlPayment(Long amount, MedicalRegistryList mrl,
			Voucher voucher);



}
