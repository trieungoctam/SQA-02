package com.spring.privateClinicManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.StatusIsApproved;

@Repository
public interface StatusIsApprovedRepository extends JpaRepository<StatusIsApproved, Integer> {
	StatusIsApproved findByStatus(String status);
}
