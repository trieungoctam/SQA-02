package com.spring.privateClinicManage.service;

import java.util.List;

import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.PrescriptionItems;

public interface PrescriptionItemsService {

	void savePrescriptionItems(PrescriptionItems prescriptionItems);

	List<PrescriptionItems> findByMedicalExamination(MedicalExamination medicalExamination);

	PrescriptionItems findById(Integer id);

}
