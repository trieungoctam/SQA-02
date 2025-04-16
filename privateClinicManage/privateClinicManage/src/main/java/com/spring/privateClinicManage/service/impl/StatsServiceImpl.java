package com.spring.privateClinicManage.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.dto.MrlAndMeHistoryDto;
import com.spring.privateClinicManage.dto.PaymentHistoryDto;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.MedicalRegistryListRepository;
import com.spring.privateClinicManage.repository.MedicineRepository;
import com.spring.privateClinicManage.repository.PaymentDetailRepository;
import com.spring.privateClinicManage.service.StatsService;

@Service
public class StatsServiceImpl implements StatsService {

	@Autowired
	private MedicineRepository medicineRepository;
	@Autowired
	private PaymentDetailRepository paymentDetailRepository;
	@Autowired
	private MedicalRegistryListRepository medicalRegistryListRepository;

	@Override
	public List<Object[]> statsByPrognosisMedicine(Integer year, Integer month) {
		return medicineRepository.statsByPrognosisMedicine(year, month);
	}

	@Override
	public List<Object[]> statsByRevenue(Integer year) {
		return paymentDetailRepository.statsByRevenue(year);
	}

	@Override
	public Page<MrlAndMeHistoryDto> paginatedStatsUserMrlAndMeHistory(Integer page, Integer size,
			User user) {

		List<MrlAndMeHistoryDto> mDto = medicalRegistryListRepository
				.statsUserMrlAndMeHistory(user);

		Pageable pageable = PageRequest.of(page - 1, size);
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), mDto.size());
		List<MrlAndMeHistoryDto> pagedMDto = mDto.subList(start, end);

		return new PageImpl<>(pagedMDto, pageable, mDto.size());
	}

	@Override
	public List<PaymentHistoryDto> statsPaymentPhase1History(String name) {
		return medicalRegistryListRepository.statsPaymentPhase1History(name);
	}

	@Override
	public List<PaymentHistoryDto> statsPaymentPhase2History(String name) {
		return medicalRegistryListRepository.statsPaymentPhase2History(name);
	}

	@Override
	public List<PaymentHistoryDto> sortByCreatedDate(List<PaymentHistoryDto> phDto) {

		return phDto.stream()
				.sorted(Comparator.comparing(PaymentHistoryDto::getCreatedDate).reversed())
				.collect(Collectors.toList());
	}
}
