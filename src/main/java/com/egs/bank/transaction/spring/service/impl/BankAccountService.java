package com.egs.bank.transaction.spring.service.impl;

import com.egs.bank.transaction.spring.entity.BankAccounts;
import com.egs.bank.transaction.spring.entity.Users;
import com.egs.bank.transaction.spring.repository.BankAccountRepository;
import com.egs.bank.transaction.spring.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;
    private final TransactionRepository transactionRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, UserService userService, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }

    public Users createBankAccount(Long adminId, String username) {
        if( userService.isLoggedIn(adminId) && userService.hasAdminRole(adminId)
                && userService.isRegisteredUser(username)) {
            BankAccounts bankAccount = new BankAccounts();
            bankAccount.setCreatedDate(LocalDate.now());
            bankAccount.setBalance(0L);
            bankAccount.setUser(userService.getUserByUsername(username));
            bankAccountRepository.save(bankAccount);
            return userService.getUserById(adminId);
        } else {
            return null;
        }
    }

    public boolean validBankAccountId(Long id) {
        if(bankAccountRepository.findById(id).get() != null) {
            return true;
        } else {
            return false;
        }
    }

    public BankAccounts getBankAccount(Long bankAccountId) {
        return bankAccountRepository.findById(bankAccountId).get();
    }

    public void depositBalance(Long transactionAmount, UUID transactionId) {
        BankAccounts bankAccount = transactionRepository.findById(transactionId).get().getBankAccount();
        Long currentBalance = bankAccount.getBalance();
        bankAccount.setBalance(currentBalance + transactionAmount);
    }

    public void withdrawalBalance(Long transactionAmount, UUID transactionId) {
        BankAccounts bankAccount = transactionRepository.findById(transactionId).get().getBankAccount();
        Long currentBalance = bankAccount.getBalance();
        bankAccount.setBalance(currentBalance - transactionAmount);
    }
}
