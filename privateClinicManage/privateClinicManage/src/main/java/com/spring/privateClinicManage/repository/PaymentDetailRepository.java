package com.spring.privateClinicManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.privateClinicManage.entity.PaymentDetail;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {

	@Query("SELECT MONTH(p.createdDate) , SUM(p.amount) " +
			"FROM PaymentDetail p " +
			"WHERE YEAR(p.createdDate) = :year " +
			"GROUP BY MONTH(p.createdDate) " +
			"ORDER BY MONTH(p.createdDate) ")
	List<Object[]> statsByRevenue(@Param("year") Integer year);
}
