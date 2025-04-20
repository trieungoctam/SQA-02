package com.spring.privateClinicManage.service.impl;

import com.spring.privateClinicManage.entity.Wallet;
import com.spring.privateClinicManage.entity.WalletHistory;
import com.spring.privateClinicManage.repository.WalletHistoryRepository;
import com.spring.privateClinicManage.service.WalletHistoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletHistoryServiceImpl implements WalletHistoryService {

    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    @Override
    @Transactional
    public void saveWalletHistory(WalletHistory walletHistory) {
        walletHistoryRepository.save(walletHistory);
    }

    @Override
    public List<WalletHistory> findByWallet(Wallet wallet) {
        return walletHistoryRepository.findByWallet(wallet);
    }
}
