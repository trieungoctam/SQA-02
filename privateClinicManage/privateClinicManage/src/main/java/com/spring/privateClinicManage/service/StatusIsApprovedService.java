package com.spring.privateClinicManage.service;

import java.util.List;

import com.spring.privateClinicManage.entity.StatusIsApproved;

public interface StatusIsApprovedService {
	void saveStatusIsApproved(StatusIsApproved statusIsApproved);

	StatusIsApproved findByStatus(String status);

	List<StatusIsApproved> findAllStatus();
}
