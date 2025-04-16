package com.spring.privateClinicManage.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.privateClinicManage.entity.Medicine;
import com.spring.privateClinicManage.entity.MedicineGroup;
import com.spring.privateClinicManage.entity.UnitMedicineType;

public interface MedicineService {
	void saveMedicine(Medicine medicine);

	List<Medicine> findAllMedicines();

	List<Medicine> findByName(String name);

	List<Medicine> sortByUtm(List<Medicine> medicines, UnitMedicineType unitMedicineType);

	List<Medicine> sortByGroup(List<Medicine> medicines, MedicineGroup medicineGroup);

	Page<Medicine> medicinesPaginated(Integer page, Integer size, List<Medicine> medicines);

	Medicine findById(Integer medicineId);

	List<Medicine> findByMedicineGroup(MedicineGroup medicineGroup);

}
