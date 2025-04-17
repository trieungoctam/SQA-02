package com.spring.privateClinicManage.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.privateClinicManage.entity.MedicineGroup;

public interface MedicineGroupService {

	void saveMedicineGroup(MedicineGroup medicineGroup);

	List<MedicineGroup> findAllMedicineGroup();

	MedicineGroup findMedicineByGroupByName(String name);

	Page<MedicineGroup> paginateMedicineGroupList(Integer size, Integer page,
			List<MedicineGroup> medicineGroups);

	MedicineGroup findMedicineGroupById(Integer medicineGroupId);
}
