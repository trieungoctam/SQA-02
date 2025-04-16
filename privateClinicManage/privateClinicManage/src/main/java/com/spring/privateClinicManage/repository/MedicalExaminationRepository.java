package com.spring.privateClinicManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;

@Repository
public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, Integer> {

	MedicalExamination findByMrl(MedicalRegistryList mrl);
}
