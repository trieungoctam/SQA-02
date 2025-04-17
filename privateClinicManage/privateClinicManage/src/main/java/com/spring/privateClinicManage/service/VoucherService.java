package com.spring.privateClinicManage.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.privateClinicManage.entity.Voucher;

public interface VoucherService {

	void saveVoucher(Voucher voucher);

	List<Voucher> findAllVouchers();

	Page<Voucher> vouchersPaginated(Integer page, Integer size, List<Voucher> vouchers);

	Voucher findVoucherById(Integer voucherId);

	List<Voucher> findAllVouchersByCodeContaining(String code);

	Voucher findVoucherByCode(String code);

}
