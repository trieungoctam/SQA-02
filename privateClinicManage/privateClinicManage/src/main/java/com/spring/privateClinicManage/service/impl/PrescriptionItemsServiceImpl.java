package com.spring.privateClinicManage.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.PrescriptionItems;
import com.spring.privateClinicManage.repository.PrescriptionItemsRepository;
import com.spring.privateClinicManage.service.PrescriptionItemsService;

import jakarta.transaction.Transactional;

@Service
public class PrescriptionItemsServiceImpl implements PrescriptionItemsService {

	@Autowired
	private PrescriptionItemsRepository prescriptionItemsRepository;

	@Override
	@Transactional
	public void savePrescriptionItems(PrescriptionItems prescriptionItems) {
		prescriptionItemsRepository.save(prescriptionItems);
	}

	@Override
	public List<PrescriptionItems> findByMedicalExamination(MedicalExamination medicalExamination) {
		return prescriptionItemsRepository.findByMedicalExamination(medicalExamination);
	}

	@Override
	public PrescriptionItems findById(Integer id) {
		Optional<PrescriptionItems> optional = prescriptionItemsRepository.findById(id);
		if (optional.isEmpty())
			return null;
		return optional.get();
	}

}
