package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.MrlVoucher;

public interface MrlVoucherService {
	void saveMrlVoucher(MrlVoucher mrlVoucher);

	MrlVoucher findByMrl(MedicalRegistryList mrl);
}
