package com.spring.privateClinicManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.UnitMedicineType;

@Repository
public interface UnitMedicineTypeRepository extends JpaRepository<UnitMedicineType, Integer>,
		PagingAndSortingRepository<UnitMedicineType, Integer> {

	UnitMedicineType findByUnitName(String unitName);
}
