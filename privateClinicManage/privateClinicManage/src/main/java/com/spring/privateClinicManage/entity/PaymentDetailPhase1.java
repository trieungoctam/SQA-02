package com.spring.privateClinicManage.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("phase1")
@Getter
@Setter
public class PaymentDetailPhase1 extends PaymentDetail {

	@OneToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	}, mappedBy = "paymentPhase1")
	@JsonIgnore
	private List<MedicalRegistryList> mrls;
}
