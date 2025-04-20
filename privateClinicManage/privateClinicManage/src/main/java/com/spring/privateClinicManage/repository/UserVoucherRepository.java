package com.spring.privateClinicManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.UserVoucher;
import com.spring.privateClinicManage.entity.Voucher;


@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, Integer> {

	UserVoucher findByUserAndVoucher(User user, Voucher voucher);
}
