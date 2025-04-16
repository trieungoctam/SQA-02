package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.VoucherCondition;

public interface VoucherConditionService {

	void saveVoucherCondition(VoucherCondition voucherCondition);

	VoucherCondition findVoucherConditionById(Integer voucherId);
}
