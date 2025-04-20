package com.spring.privateClinicManage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.UserVoucher;
import com.spring.privateClinicManage.entity.Voucher;
import com.spring.privateClinicManage.repository.UserVoucherRepository;
import com.spring.privateClinicManage.service.UserVoucherService;

import jakarta.transaction.Transactional;

@Service
public class UserVoucherServiceImpl implements UserVoucherService {

	@Autowired
	private UserVoucherRepository userVoucherRepository;

	@Override
	@Transactional
	public void saveUserVoucher(UserVoucher userVoucher) {
		userVoucherRepository.save(userVoucher);
	}

	@Override
	public UserVoucher findByUserAndVoucher(User user, Voucher voucher) {
		return userVoucherRepository.findByUserAndVoucher(user, voucher);
	}

}
