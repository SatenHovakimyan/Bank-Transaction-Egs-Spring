package com.egs.bank.transaction.system.service.impl;

import com.egs.bank.transaction.system.entity.BankAccounts;
import com.egs.bank.transaction.system.entity.Users;
import com.egs.bank.transaction.system.repository.BankAccountRepository;
import com.egs.bank.transaction.system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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

    public BankAccounts createBankAccount(Long adminId, String username) {
        if (userService.isLoggedIn(adminId) && userService.hasAdminRole(adminId)
                && userService.isRegisteredUser(username)) {
            BankAccounts bankAccount = new BankAccounts();
            bankAccount.setCreatedDate(LocalDate.now());
            bankAccount.setBalance(0L);
            Users userOfBankAccount = userService.getUserByUsername(username);
            bankAccount.setUser(userOfBankAccount);
            bankAccountRepository.save(bankAccount);
            return bankAccount;
        } else {
            return null;
        }
    }

    public boolean validBankAccountId(Long id) {
        return bankAccountRepository.findById(id).isPresent();
    }

    public BankAccounts getBankAccount(Long bankAccountId) {
        return bankAccountRepository.findById(bankAccountId).orElseThrow(EntityNotFoundException::new);
    }

    public void depositBalance(Long transactionAmount, UUID transactionId) {

        BankAccounts bankAccount = transactionRepository.
                findById(transactionId).
                orElseThrow(EntityNotFoundException::new).
                getBankAccount();
        Long currentBalance = bankAccount.getBalance();
        bankAccount.setBalance(currentBalance + transactionAmount);
    }

    public void withdrawalBalance(Long transactionAmount, UUID transactionId) {
        BankAccounts bankAccount = transactionRepository.
                findById(transactionId).
                orElseThrow(EntityNotFoundException::new).
                getBankAccount();
        Long currentBalance = bankAccount.getBalance();
        bankAccount.setBalance(currentBalance - transactionAmount);
    }
}
