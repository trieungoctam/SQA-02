package com.spring.privateClinicManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.VerifyEmail;


@Repository
public interface VerifyEmailRepository extends JpaRepository<VerifyEmail, Integer> {

	VerifyEmail findByEmail(String email);
}
