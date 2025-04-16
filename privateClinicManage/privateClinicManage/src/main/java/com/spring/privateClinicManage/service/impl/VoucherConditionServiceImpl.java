package com.spring.privateClinicManage.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.VoucherCondition;
import com.spring.privateClinicManage.repository.VoucherConditionRepository;
import com.spring.privateClinicManage.service.VoucherConditionService;

import jakarta.transaction.Transactional;

@Service
public class VoucherConditionServiceImpl implements VoucherConditionService {

	@Autowired
	private VoucherConditionRepository voucherConditionRepository;

	@Override
	@Transactional
	public void saveVoucherCondition(VoucherCondition voucherCondition) {
		voucherConditionRepository.save(voucherCondition);
	}

	@Override
	public VoucherCondition findVoucherConditionById(Integer voucherId) {
		Optional<VoucherCondition> optional = voucherConditionRepository.findById(voucherId);
		if (optional.isEmpty())
			return null;

		return optional.get();
	}

}
