package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;

public interface MedicalExaminationService {

	void saveMedicalExamination(MedicalExamination medicalExamination);

	MedicalExamination findByMrl(MedicalRegistryList mrl);

	MedicalExamination findById(Integer id);

}
