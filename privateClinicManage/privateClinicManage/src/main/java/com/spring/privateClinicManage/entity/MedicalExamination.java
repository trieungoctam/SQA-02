package com.spring.privateClinicManage.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medical_examination")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalExamination implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "predict")
	private String predict;

	@Column(name = "advance")
	private String advance;

	@Column(name = "symptom_process", nullable = false)
	private String symptomProcess;

	@Column(name = "treatment_process", nullable = false)
	private String treatmentProcess;

	@Column(name = "duration_day")
	private Integer durationDay;

	@Column(name = "follow_up_date")
	private Date followUpDate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinColumn(name = "user_created_id", referencedColumnName = "id")
	private User userCreated;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "medical_register_list_id", referencedColumnName = "id")
	private MedicalRegistryList mrl;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "medicalExamination")
	@JsonIgnore
	private List<PrescriptionItems> prescriptionItems;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "payment_phase2_id", referencedColumnName = "id")
	@JsonIgnore
	private PaymentDetailPhase2 paymentPhase2;

}
