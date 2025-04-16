package com.spring.privateClinicManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.spring.privateClinicManage.entity.MedicineGroup;


public interface MedicineGroupRepository extends JpaRepository<MedicineGroup, Integer>,
		PagingAndSortingRepository<MedicineGroup, Integer> {

	MedicineGroup findByGroupName(String groupName);
}
