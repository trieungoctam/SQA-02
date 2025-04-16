package com.spring.privateClinicManage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.PaymentDetailPhase1;
import com.spring.privateClinicManage.repository.PaymentDetailPhase1Repository;
import com.spring.privateClinicManage.service.PaymentDetailPhase1Service;

import jakarta.transaction.Transactional;

@Service
public class PaymentDetailPhase1ServiceImpl implements PaymentDetailPhase1Service {

	@Autowired
	private PaymentDetailPhase1Repository paymentDetailPhase1Repository;

	@Override
	@Transactional
	public void savePdp1(PaymentDetailPhase1 paymentDetailPhase1) {
		paymentDetailPhase1Repository.save(paymentDetailPhase1);
	}

}
