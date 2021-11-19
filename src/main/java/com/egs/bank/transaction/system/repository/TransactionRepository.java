package com.egs.bank.transaction.system.repository;

import com.egs.bank.transaction.system.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, UUID> {
        @Query("SELECT transaction FROM Transactions transaction WHERE transaction.bankAccount.id = :bankAccountId")
        Set<Transactions> findByBankAccount(Long bankAccountId);
}
