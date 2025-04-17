package com.spring.privateClinicManage.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medicalRegistryList")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRegistryList implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "favor", nullable = false)
	private String favor;

	@Column(name = "qlUrl")
	private String qrUrl;

	@Column(name = "is_canceled", nullable = false)
	private Boolean isCanceled;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "is_voucher_taken")
	private Boolean isVoucherTaken = false;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinColumn(name = "schedule_id", referencedColumnName = "id")
//	@JsonIgnore
	private Schedule schedule;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinColumn(name = "user_id", referencedColumnName = "id")
//	@JsonIgnore
	private User user;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinColumn(name = "statusIsApproved_id", referencedColumnName = "id")
//	@JsonIgnore
	private StatusIsApproved statusIsApproved;

	@Transient
	private Integer order;

	@Transient
	@JsonIgnore
	private MultipartFile file;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "mrl")
	@JsonIgnore
	private MedicalExamination medicalExamination;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "payment_phase1_id", referencedColumnName = "id")
	@JsonIgnore
	private PaymentDetailPhase1 paymentPhase1;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mrl")
	@JsonIgnore
	private List<MrlVoucher> mrlVoucher;
}
