package com.spring.privateClinicManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.PaymentDetailPhase1;

@Repository
public interface PaymentDetailPhase1Repository extends JpaRepository<PaymentDetailPhase1, Integer> {

}
