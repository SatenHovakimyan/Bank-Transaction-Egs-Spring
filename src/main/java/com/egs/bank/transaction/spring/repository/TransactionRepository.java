package com.egs.bank.transaction.spring.repository;

import com.egs.bank.transaction.spring.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, UUID> {

}
