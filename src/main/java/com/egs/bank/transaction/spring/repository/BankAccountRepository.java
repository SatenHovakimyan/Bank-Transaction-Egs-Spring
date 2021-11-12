package com.egs.bank.transaction.spring.repository;

import com.egs.bank.transaction.spring.entity.BankAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccounts, Long> {
    
}
