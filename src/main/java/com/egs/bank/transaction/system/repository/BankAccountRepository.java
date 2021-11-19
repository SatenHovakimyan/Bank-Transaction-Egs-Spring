package com.egs.bank.transaction.system.repository;

import com.egs.bank.transaction.system.entity.BankAccounts;
import com.egs.bank.transaction.system.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccounts, Long> {

    @Query("SELECT bankAccount FROM BankAccounts bankAccount WHERE bankAccount.user.id = :userId")
    Set<BankAccounts> findByUser(Long userId);
}
