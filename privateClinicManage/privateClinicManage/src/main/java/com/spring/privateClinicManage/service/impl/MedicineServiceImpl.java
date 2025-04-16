package com.spring.privateClinicManage.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.Medicine;
import com.spring.privateClinicManage.entity.MedicineGroup;
import com.spring.privateClinicManage.entity.UnitMedicineType;
import com.spring.privateClinicManage.repository.MedicineRepository;
import com.spring.privateClinicManage.service.MedicineService;

import jakarta.transaction.Transactional;

@Service
public class MedicineServiceImpl implements MedicineService {

	@Autowired
	private MedicineRepository medicineRepository;

	@Override
	@Transactional
	public void saveMedicine(Medicine medicine) {
		medicineRepository.save(medicine);
	}

	@Override
	public List<Medicine> findByName(String name) {
		return medicineRepository.findByName(name);
	}

	@Override
	public List<Medicine> sortByUtm(List<Medicine> medicines, UnitMedicineType unitMedicineType) {
		return medicines.stream()
				.filter(m -> m.getUnitType().equals(unitMedicineType))
				.collect(Collectors.toList());
	}

	@Override
	public List<Medicine> sortByGroup(List<Medicine> medicines, MedicineGroup medicineGroup) {
		return medicines.stream()
				.filter(m -> m.getMedicineGroup().equals(medicineGroup))
				.collect(Collectors.toList());
	}

	@Override
	public List<Medicine> findAllMedicines() {
		return medicineRepository.findAll();
	}

	@Override
	public Page<Medicine> medicinesPaginated(Integer page, Integer size, List<Medicine> medicines) {
		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = 0;
		List<Medicine> medicinesPaginated;

		if (medicines.size() < start) {
			medicinesPaginated = Collections.emptyList();
		} else {
			end = Math.min((start + pageable.getPageSize()), medicines.size());
			medicinesPaginated = medicines.subList(start, end);
		}

		return new PageImpl<>(medicinesPaginated, pageable, medicines.size());
	}

	@Override
	public Medicine findById(Integer medicineId) {
		Optional<Medicine> optional = medicineRepository.findById(medicineId);
		if (optional.isEmpty())
			return null;

		return optional.get();
	}

	@Override
	public List<Medicine> findByMedicineGroup(MedicineGroup medicineGroup) {
		return medicineRepository.findByMedicineGroup(medicineGroup);
	}

}
