package com.spring.privateClinicManage.service.impl;

import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.Wallet;
import com.spring.privateClinicManage.repository.WalletRepository;
import com.spring.privateClinicManage.service.WallerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WallerService {

    @Autowired
    private WalletRepository walletRepository;


    @Override
    @Transactional
    public void saveWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user);
    }
}
