package com.spring.privateClinicManage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.MrlVoucher;
import com.spring.privateClinicManage.repository.MrlVoucherRepository;
import com.spring.privateClinicManage.service.MrlVoucherService;

import jakarta.transaction.Transactional;

@Service
public class MrlVoucherServiceImpl implements MrlVoucherService {

	@Autowired
	private MrlVoucherRepository mrlVoucherRepository;

	@Override
	@Transactional
	public void saveMrlVoucher(MrlVoucher mrlVoucher) {
		mrlVoucherRepository.save(mrlVoucher);
	}

	@Override
	public MrlVoucher findByMrl(MedicalRegistryList mrl) {
		return mrlVoucherRepository.findByMrl(mrl);
	}

}
