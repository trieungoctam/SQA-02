package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.AttendanceExerciseRecord;
import com.spring.privateClinicManage.entity.User;

public interface AttendanceExerciseRecordService {

    void saveAttendanceExerciseRecord(AttendanceExerciseRecord attendanceExerciseRecord);

    AttendanceExerciseRecord findAttendanceExerciseRecordByClockIn(int year, int month, int day, User user);

    Integer totalPeriodAttendanceExerciseRecordByUser(User user);
}
