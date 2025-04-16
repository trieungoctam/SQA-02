package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.Wallet;

public interface WallerService {

    void saveWallet(Wallet wallet);
    Wallet findByUser(User user);
}
