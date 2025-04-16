package com.spring.privateClinicManage.repository;

import com.spring.privateClinicManage.entity.AttendanceExerciseRecord;
import com.spring.privateClinicManage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AttendanceExerciseRecordRepository extends JpaRepository<AttendanceExerciseRecord, Integer> {

    @Query("SELECT a FROM AttendanceExerciseRecord a WHERE a.user = :user AND a.createdTime BETWEEN :startDate AND :endDate")
    AttendanceExerciseRecord findAttendanceExerciseRecordByDateRange(LocalDateTime startDate
            , LocalDateTime endDate , User user);

    @Query("SELECT SUM(a.period) FROM AttendanceExerciseRecord a " +
           "WHERE a.user = :user ")
    Integer totalPeriodAttendanceExerciseRecordByUser(User user);
}
