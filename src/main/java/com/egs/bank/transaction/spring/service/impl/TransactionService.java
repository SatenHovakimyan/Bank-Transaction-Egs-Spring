package com.egs.bank.transaction.spring.service.impl;

import com.egs.bank.transaction.spring.entity.BankAccounts;
import com.egs.bank.transaction.spring.entity.Transactions;
import com.egs.bank.transaction.spring.enums.TransactionStatus;
import com.egs.bank.transaction.spring.enums.TransactionType;
import com.egs.bank.transaction.spring.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final BankAccountService bankAccountService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserService userService, BankAccountService bankAccountService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }


    public Transactions createDeposit(Long userId, Long bankAccountId, Long transactionAmount) {
        return createTransaction(userId, bankAccountId, transactionAmount, TransactionType.DEPOSIT);
    }


    public Transactions createWithdrawal(Long userId, Long bankAccountId, Long transactionAmount) {
        return createTransaction(userId, bankAccountId, transactionAmount, TransactionType.WITHDRAWAL);
    }

    private Transactions createTransaction(Long userId, Long bankAccountId, Long transactionAmount,
                                        TransactionType transactionType) {
        Transactions transaction = new Transactions();
        if (userService.isLoggedIn(userId) && bankAccountService.validBankAccountId(bankAccountId) &&
                transactionAmount >= 0L) {
            transaction.setCreatedData(LocalDateTime.now());
            transaction.setTransactionType(transactionType);
            transaction.setTransactionAmount(transactionAmount);
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            //transaction.setBankAccount(bankAccountService.getBankAccount(bankAccountId));
            //transaction.setUser(userService.getUserById(userId));
            transactionRepository.save(transaction);
            return transaction;

        } else {
            return null;
        }
    }

    public List<Transactions> getTransactions(Long adminId) {
        if(userService.hasAdminRole(adminId)) {
            return transactionRepository.findAll();
        } else {
            return null;
        }
    }

    public void acceptTransaction(Long adminId, UUID transactionId) {
        if(userService.hasAdminRole(adminId) && transactionRepository.findById(transactionId).get() != null) {
            Transactions transaction = transactionRepository.findById(transactionId).get();
            transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
            if(transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {
                bankAccountService.depositBalance(transaction.getTransactionAmount(), transaction.getId());
            } else {
                bankAccountService.withdrawalBalance(transaction.getTransactionAmount(), transaction.getId());
            }
            transactionRepository.save(transaction);
        }
    }
}
