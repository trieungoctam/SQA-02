package com.spring.privateClinicManage.repository;

import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Wallet findByUser(User user);
}
