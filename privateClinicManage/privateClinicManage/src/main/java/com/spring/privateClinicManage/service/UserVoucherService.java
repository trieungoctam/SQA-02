package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.UserVoucher;
import com.spring.privateClinicManage.entity.Voucher;

public interface UserVoucherService {

	void saveUserVoucher(UserVoucher userVoucher);

	UserVoucher findByUserAndVoucher(User user, Voucher voucher);
}
