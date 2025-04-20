package com.spring.privateClinicManage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.PaymentDetailPhase2;
import com.spring.privateClinicManage.repository.PaymentDetailPhase2Repository;
import com.spring.privateClinicManage.service.PaymentDetailPhase2Service;

import jakarta.transaction.Transactional;

@Service
public class PaymentDetailPhase2ServiceImpl implements PaymentDetailPhase2Service {

	@Autowired
	private PaymentDetailPhase2Repository paymentDetailPhase2Repository;

	@Override
	@Transactional
	public void savePdp2(PaymentDetailPhase2 paymentDetailPhase2) {
		paymentDetailPhase2Repository.save(paymentDetailPhase2);
	}

}
