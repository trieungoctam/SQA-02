package com.spring.privateClinicManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.PrescriptionItems;

@Repository
public interface PrescriptionItemsRepository extends JpaRepository<PrescriptionItems, Integer> {

	List<PrescriptionItems> findByMedicalExamination(MedicalExamination medicalExamination);
}
