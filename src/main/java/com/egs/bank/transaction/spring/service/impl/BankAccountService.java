package com.egs.bank.transaction.spring.service.impl;

import com.egs.bank.transaction.spring.entity.BankAccounts;
import com.egs.bank.transaction.spring.entity.Users;
import com.egs.bank.transaction.spring.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
    }

    public Users createBankAccount(Long adminId, String username) {
        if( userService.isLoggedIn(adminId) && userService.hasAdminRole(adminId)
                && userService.isRegisteredUser(username)) {
            BankAccounts bankAccount = new BankAccounts();
            bankAccount.setCreatedDate(LocalDate.now());
            bankAccount.setBalance(0L);
            bankAccount.setUser(userService.getUserByUsername(username));
            return userService.getUserById(adminId);
        } else {
            return null;
        }
    }
}
