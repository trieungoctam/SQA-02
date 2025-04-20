package com.spring.privateClinicManage.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

	Schedule findByDate(Date date);

	@Query("SELECT s FROM Schedule s " +
			"WHERE YEAR(s.date) = :year and MONTH(s.date) = :month and DAY(s.date) = :day ")
	Schedule findByDayMonthYear(@Param("year") Integer year, @Param("month") Integer month,
			@Param("day") Integer day);
}
