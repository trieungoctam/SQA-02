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

import com.spring.privateClinicManage.entity.MedicineGroup;
import com.spring.privateClinicManage.repository.MedicineGroupRepository;
import com.spring.privateClinicManage.service.MedicineGroupService;

import jakarta.transaction.Transactional;

@Service
public class MedicineGroupServiceImpl implements MedicineGroupService {

	@Autowired
	private MedicineGroupRepository medicineGroupRepository;

	@Override
	@Transactional
	public void saveMedicineGroup(MedicineGroup medicineGroup) {
		medicineGroupRepository.save(medicineGroup);
	}

	@Override
	public List<MedicineGroup> findAllMedicineGroup() {
		return medicineGroupRepository.findAll();
	}

	@Override
	public MedicineGroup findMedicineByGroupByName(String name) {
		return medicineGroupRepository.findByGroupName(name);
	}

	@Override
	public Page<MedicineGroup> paginateMedicineGroupList(Integer size, Integer page,
			List<MedicineGroup> medicineGroups) {

		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = 0;
		List<MedicineGroup> medicineGroupsPaginated;

		if (medicineGroups.size() < start) {
			medicineGroupsPaginated = Collections.emptyList();
		} else {
			end = Math.min((start + pageable.getPageSize()), medicineGroups.size());
			medicineGroupsPaginated = medicineGroups.subList(start, end);
		}

		return new PageImpl<>(medicineGroupsPaginated, pageable, medicineGroups.size());
	}

	@Override
	public MedicineGroup findMedicineGroupById(Integer medicineGroupId) {
		Optional<MedicineGroup> optionals = medicineGroupRepository.findById(medicineGroupId);

		if (optionals.isEmpty())
			return null;

		return optionals.get();
	}

}
