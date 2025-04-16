package com.spring.privateClinicManage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.repository.StatusIsApprovedRepository;
import com.spring.privateClinicManage.service.StatusIsApprovedService;

import jakarta.transaction.Transactional;

@Service
public class StatusIsApprovedServiceImpl implements StatusIsApprovedService {

	@Autowired
	private StatusIsApprovedRepository statusIsApprovedRepository;

	@Override
	@Transactional
	public void saveStatusIsApproved(StatusIsApproved statusIsApproved) {
		statusIsApprovedRepository.save(statusIsApproved);
	}

	@Override
	public StatusIsApproved findByStatus(String status) {
		return statusIsApprovedRepository.findByStatus(status);
	}

	@Override
	public List<StatusIsApproved> findAllStatus() {
		return statusIsApprovedRepository.findAll();
	}

}
