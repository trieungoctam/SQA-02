package com.spring.privateClinicManage.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.privateClinicManage.entity.UnitMedicineType;

public interface UnitMedicineTypeService {
	void saveUnitMedicineType(UnitMedicineType unitMedicineType);

	List<UnitMedicineType> findAllUmt();

	UnitMedicineType findUtmByUnitName(String unitName);

	Page<UnitMedicineType> paginateUmtList(Integer size, Integer page, List<UnitMedicineType> umts);

	UnitMedicineType findUtmById(Integer utmId);
}
