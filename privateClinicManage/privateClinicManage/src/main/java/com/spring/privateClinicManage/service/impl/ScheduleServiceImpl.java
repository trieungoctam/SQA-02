package com.spring.privateClinicManage.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.repository.ScheduleRepository;
import com.spring.privateClinicManage.service.ScheduleService;

import jakarta.transaction.Transactional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Override
	@Transactional
	public void saveSchedule(Schedule schedule) {
		scheduleRepository.save(schedule);
	}

	@Override
	public Schedule findByDate(Date date) {
		return scheduleRepository.findByDate(date);
	}

	@Override
	public Schedule findById(Integer id) {
		Optional<Schedule> optional = scheduleRepository.findById(id);
		if (optional.isEmpty())
			return null;
		return optional.get();
	}

	@Override
	public Schedule findByDayMonthYear(Integer year, Integer month, Integer day) {
		return scheduleRepository.findByDayMonthYear(year, month, day);
	}

	@Override
	public List<Schedule> findAllSchedule() {
		return scheduleRepository.findAll();
	}

	@Override
	public Page<Schedule> schedulePaginated(Integer page, Integer size, List<Schedule> schedules) {
		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = 0;
		List<Schedule> schedulesPaginated;

		if (schedules.size() < start) {
			schedulesPaginated = Collections.emptyList();
		} else {
			end = Math.min((start + pageable.getPageSize()), schedules.size());
			schedulesPaginated = schedules.subList(start, end);
		}

		return new PageImpl<>(schedulesPaginated, pageable, schedules.size());
	}

}
