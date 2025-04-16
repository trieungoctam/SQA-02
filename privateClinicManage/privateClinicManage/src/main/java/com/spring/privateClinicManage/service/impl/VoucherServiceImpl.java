package com.spring.privateClinicManage.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.Voucher;
import com.spring.privateClinicManage.entity.VoucherCondition;
import com.spring.privateClinicManage.repository.VoucherRepository;
import com.spring.privateClinicManage.service.VoucherConditionService;
import com.spring.privateClinicManage.service.VoucherService;

import jakarta.transaction.Transactional;

@Service
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	private VoucherRepository voucherRepository;
	@Autowired
	private VoucherConditionService voucherConditionService;

	@Override
	@Transactional
	public void saveVoucher(Voucher voucher) {

		if (voucher.getVoucherCondition().getId() != null) {
			VoucherCondition voucherCondition = voucherConditionService
					.findVoucherConditionById(voucher.getVoucherCondition().getId());
			if (voucherCondition != null) {
				voucher.setVoucherCondition(voucherCondition);
			}
		}

		voucherRepository.save(voucher);
	}

	@Override
	public List<Voucher> findAllVouchersByCodeContaining(String code) {
		return voucherRepository.findByCodeContaining(code);
	}

	@Override
	public List<Voucher> findAllVouchers() {
		return voucherRepository.findAll();
	}

	@Override
	public Page<Voucher> vouchersPaginated(Integer page, Integer size, List<Voucher> vouchers) {
		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = 0;
		List<Voucher> vouchersPaginated;

		if (vouchers.size() < start) {
			vouchersPaginated = Collections.emptyList();
		} else {
			end = Math.min((start + pageable.getPageSize()), vouchers.size());
			vouchersPaginated = vouchers.subList(start, end);
		}

		return new PageImpl<>(vouchersPaginated, pageable, vouchers.size());
	}

	@Override
	public Voucher findVoucherById(Integer voucherId) {
		Optional<Voucher> optional = voucherRepository.findById(voucherId);
		if (optional == null)
			return null;
		return optional.get();
	}

	@Override
	public Voucher findVoucherByCode(String code) {
		return voucherRepository.findByCode(code);
	}

}
