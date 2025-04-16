package com.spring.privateClinicManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.dto.MrlAndMeHistoryDto;
import com.spring.privateClinicManage.dto.PaymentHistoryDto;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;

@Repository
public interface MedicalRegistryListRepository extends JpaRepository<MedicalRegistryList, Integer>,
		PagingAndSortingRepository<MedicalRegistryList, Integer> {

	@Query("SELECT mrl FROM MedicalRegistryList mrl WHERE mrl.user = :user and mrl.schedule = :schedule")
	MedicalRegistryList findMRLByUserAndSchedule(@Param("user") User user,
			@Param("schedule") Schedule schedule);

	@Query("SELECT COUNT(mrl) FROM MedicalRegistryList mrl " +
			"WHERE mrl.user = :user and mrl.schedule = :schedule and mrl.isCanceled = :isCanceled "
			+
			"and mrl.statusIsApproved = :statusIsApproved ")
	Integer countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(@Param("user") User user,
			@Param("schedule") Schedule schedule, @Param("isCanceled") Boolean isCanceled,
			@Param("statusIsApproved") StatusIsApproved statusIsApproved);

	List<MedicalRegistryList> findByUser(User user);

	@Query("SELECT mrl FROM MedicalRegistryList mrl " +
			"LEFT JOIN mrl.schedule s " +
			"WHERE YEAR(s.date) = :year and MONTH(s.date) = :month and DAY(s.date) = :day " +
			"and mrl.statusIsApproved = :statusIsApproved ")
	List<MedicalRegistryList> findByScheduleAndStatusIsApproved(
			@Param("year") Integer year, @Param("month") Integer month,
			@Param("day") Integer day,
			@Param("statusIsApproved") StatusIsApproved status);

	@Query("SELECT mrl FROM MedicalRegistryList mrl " +
			"WHERE mrl.schedule = :schedule and mrl.statusIsApproved = :statusIsApproved ")
	List<MedicalRegistryList> findByScheduleAndStatusIsApproved2(
			@Param("schedule") Schedule schedule,
			@Param("statusIsApproved") StatusIsApproved status);

	@Query("SELECT mrl FROM MedicalRegistryList mrl " +
			"LEFT JOIN mrl.user u " +
			"WHERE u.name LIKE %:key% "
			+ "OR u.phone LIKE %:key% "
			+ "OR u.address LIKE %:key% "
			+ "OR u.email LIKE %:key% ")
	List<MedicalRegistryList> findByAnyKey(@Param("key") String key);

	@Query("SELECT m FROM MedicalRegistryList m " +
			"WHERE YEAR(m.createdDate) = :year " +
			"AND MONTH(m.createdDate) = :month " +
			"AND DAY(m.createdDate) = :day " +
			"AND m IN :mrls")
	List<MedicalRegistryList> sortByCreatedDate(@Param("mrls") List<MedicalRegistryList> mrls,
			@Param("year") Integer year,
			@Param("month") Integer month,
			@Param("day") Integer day);

	@Query("SELECT mrl.user FROM MedicalRegistryList mrl " +
			"WHERE mrl.statusIsApproved = :status and mrl.schedule = :schedule " +
			"GROUP BY mrl.user ")
	List<User> findUniqueUser(@Param("schedule") Schedule schedule,
			@Param("status") StatusIsApproved status);

	@Query("SELECT COUNT(mrl) FROM MedicalRegistryList mrl WHERE mrl.schedule = :schedule " +
			"AND mrl.statusIsApproved IN :statuses")
	Integer countMRLByScheduleAndStatuses(
			@Param("schedule") Schedule schedule,
			@Param("statuses") List<StatusIsApproved> statuses);

	@Query("SELECT mrl FROM MedicalRegistryList mrl " +
			"WHERE mrl.user = :user and mrl.name = :nameRegister")
	List<MedicalRegistryList> findAllMrlByUserAndName(@Param("user") User user,
			@Param("nameRegister") String nameRegister);

	@Query("SELECT new com.spring.privateClinicManage.dto.MrlAndMeHistoryDto(mrl.name , MAX(me.createdDate) , COUNT(me.id)) "
			+
			"FROM MedicalRegistryList mrl " +
			"LEFT join mrl.medicalExamination me " +
			"WHERE mrl.user = :user " +
			"GROUP BY mrl.name ")
	List<MrlAndMeHistoryDto> statsUserMrlAndMeHistory(@Param("user") User user);

	@Query("SELECT new com.spring.privateClinicManage.dto.PaymentHistoryDto " +
			"(pmp1.orderId , pmp1.createdDate , mrl.name, pmp1.amount , pmp1.description , pmp1.resultCode , pmp1.partnerCode) "
			+
			"FROM MedicalRegistryList mrl " +
			"INNER JOIN mrl.paymentPhase1 pmp1 " +
			"WHERE mrl.name = :name ")
	List<PaymentHistoryDto> statsPaymentPhase1History(@Param("name") String name);

	@Query("SELECT new com.spring.privateClinicManage.dto.PaymentHistoryDto " +
			"(pmp2.orderId , pmp2.createdDate , mrl.name, pmp2.amount , pmp2.description , pmp2.resultCode , pmp2.partnerCode) "
			+
			"FROM MedicalRegistryList mrl " +
			"INNER JOIN mrl.medicalExamination me " +
			"INNER JOIN me.paymentPhase2 pmp2 " +
			"WHERE mrl.name = :name ")
	List<PaymentHistoryDto> statsPaymentPhase2History(@Param("name") String name);

}
