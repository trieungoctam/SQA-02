package com.spring.privateClinicManage.repository;

import com.spring.privateClinicManage.entity.Wallet;
import com.spring.privateClinicManage.entity.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Integer> {

    List<WalletHistory> findByWallet(Wallet wallet);
}
