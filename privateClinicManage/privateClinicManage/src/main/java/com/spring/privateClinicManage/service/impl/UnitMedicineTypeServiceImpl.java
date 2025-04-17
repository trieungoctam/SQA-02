package com.spring.privateClinicManage.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.UnitMedicineType;
import com.spring.privateClinicManage.repository.UnitMedicineTypeRepository;
import com.spring.privateClinicManage.service.UnitMedicineTypeService;

import jakarta.transaction.Transactional;

@Service
public class UnitMedicineTypeServiceImpl implements UnitMedicineTypeService {

	@Autowired
	private UnitMedicineTypeRepository unitMedicineTypeRepository;

	@Override
	@Transactional
	public void saveUnitMedicineType(UnitMedicineType unitMedicineType) {
		unitMedicineTypeRepository.save(unitMedicineType);
	}

	@Override
	public List<UnitMedicineType> findAllUmt() {
		return unitMedicineTypeRepository.findAll();
	}

	@Override
	public UnitMedicineType findUtmByUnitName(String unitName) {
		return unitMedicineTypeRepository.findByUnitName(unitName);
	}

	@Override
	public Page<UnitMedicineType> paginateUmtList(Integer size, Integer page,
			List<UnitMedicineType> umts) {

		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = 0;
		List<UnitMedicineType> umtsPaginated;

		if (umts.size() < start) {
			umtsPaginated = Collections.emptyList();
		} else {
			end = Math.min((start + pageable.getPageSize()), umts.size());
			umtsPaginated = umts.subList(start, end);
		}

		return new PageImpl<>(umtsPaginated, pageable, umts.size());
	}

	@Override
	public UnitMedicineType findUtmById(Integer utmId) {
		Optional<UnitMedicineType> optional = unitMedicineTypeRepository.findById(utmId);
		if (optional.isEmpty())
			return null;

		return optional.get();
	}
}
