package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.Wallet;
import com.spring.privateClinicManage.entity.WalletHistory;

import java.util.List;

public interface WalletHistoryService {
    void saveWalletHistory(WalletHistory walletHistory);

    List<WalletHistory> findByWallet(Wallet wallet);
}
