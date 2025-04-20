package com.spring.privateClinicManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.MrlVoucher;

@Repository
public interface MrlVoucherRepository extends JpaRepository<MrlVoucher, Integer> {
	MrlVoucher findByMrl(MedicalRegistryList mrl);
}
