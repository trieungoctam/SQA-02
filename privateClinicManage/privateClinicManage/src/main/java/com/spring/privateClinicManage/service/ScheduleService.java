package com.spring.privateClinicManage.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.privateClinicManage.entity.Schedule;

public interface ScheduleService {
	void saveSchedule(Schedule schedule);

	Schedule findByDate(Date date);

	Schedule findById(Integer id);

	Schedule findByDayMonthYear(Integer year, Integer month, Integer day);

	List<Schedule> findAllSchedule();

	Page<Schedule> schedulePaginated(Integer page, Integer size, List<Schedule> schedules);

}
