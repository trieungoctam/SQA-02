package com.spring.privateClinicManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.VoucherCondition;

@Repository
public interface VoucherConditionRepository extends JpaRepository<VoucherCondition, Integer> {

}
