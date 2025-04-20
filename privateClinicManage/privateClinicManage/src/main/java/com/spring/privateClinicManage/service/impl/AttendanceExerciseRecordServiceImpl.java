package com.spring.privateClinicManage.service.impl;

import com.spring.privateClinicManage.entity.AttendanceExerciseRecord;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.AttendanceExerciseRecordRepository;
import com.spring.privateClinicManage.service.AttendanceExerciseRecordService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AttendanceExerciseRecordServiceImpl implements AttendanceExerciseRecordService {

    @Autowired
    private AttendanceExerciseRecordRepository attendanceExerciseRecordRepository;

    @Override
    @Transactional
    public void saveAttendanceExerciseRecord(AttendanceExerciseRecord attendanceExerciseRecord) {
        attendanceExerciseRecordRepository.save(attendanceExerciseRecord);
    }

    @Override
    public AttendanceExerciseRecord findAttendanceExerciseRecordByClockIn(int year, int month, int day, User user) {
        LocalDateTime startDate = LocalDateTime.of(year, month, day, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, month, day, 23, 59, 59, 999999999);
        return attendanceExerciseRecordRepository.findAttendanceExerciseRecordByDateRange(startDate, endDate, user);
    }

    @Override
    public Integer totalPeriodAttendanceExerciseRecordByUser(User user) {
        return attendanceExerciseRecordRepository.totalPeriodAttendanceExerciseRecordByUser(user);
    }

}
