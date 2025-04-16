package com.spring.privateClinicManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

	List<Voucher> findByCodeContaining(String code);

	Voucher findByCode(String code);
}
