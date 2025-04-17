package com.spring.privateClinicManage.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.repository.MedicalExaminationRepository;
import com.spring.privateClinicManage.service.MedicalExaminationService;

import jakarta.transaction.Transactional;

@Service
public class MedicalExaminationServiceImpl implements MedicalExaminationService {

	@Autowired
	private MedicalExaminationRepository medicalExaminationRepository;

	@Override
	@Transactional
	public void saveMedicalExamination(MedicalExamination medicalExamination) {
		medicalExaminationRepository.save(medicalExamination);
	}

	@Override
	public MedicalExamination findByMrl(MedicalRegistryList mrl) {
		return medicalExaminationRepository.findByMrl(mrl);
	}

	@Override
	public MedicalExamination findById(Integer id) {
		Optional<MedicalExamination> optional = medicalExaminationRepository.findById(id);
		if (optional.isEmpty())
			return null;
		return optional.get();
	}

}
