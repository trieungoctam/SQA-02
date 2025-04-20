package com.spring.privateClinicManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.spring.privateClinicManage.entity.Medicine;
import com.spring.privateClinicManage.entity.MedicineGroup;

public interface MedicineRepository
		extends JpaRepository<Medicine, Integer>, PagingAndSortingRepository<Medicine, Integer> {

	List<Medicine> findByName(String name);

	List<Medicine> findByMedicineGroup(MedicineGroup medicineGroup);

	@Query("SELECT m.name , SUM(p.prognosis) " +
			"FROM Medicine m " +
			"INNER JOIN m.ptis p " +
			"INNER JOIN p.medicalExamination me " +
			"WHERE YEAR(me.createdDate) = :year and MONTH(me.createdDate) = :month " +
			"GROUP BY m.name ")
	List<Object[]> statsByPrognosisMedicine(@Param("year") Integer year,
			@Param("month") Integer month);

}
