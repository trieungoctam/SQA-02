package com.spring.privateClinicManage.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.privateClinicManage.dto.MrlAndMeHistoryDto;
import com.spring.privateClinicManage.dto.PaymentHistoryDto;
import com.spring.privateClinicManage.entity.User;

public interface StatsService {

	List<Object[]> statsByPrognosisMedicine(Integer year, Integer month);

	List<Object[]> statsByRevenue(Integer year);

	Page<MrlAndMeHistoryDto> paginatedStatsUserMrlAndMeHistory(Integer page, Integer size,
			User user);

	List<PaymentHistoryDto> statsPaymentPhase1History(String name);

	List<PaymentHistoryDto> statsPaymentPhase2History(String name);

	List<PaymentHistoryDto> sortByCreatedDate(List<PaymentHistoryDto> phDto);

}
