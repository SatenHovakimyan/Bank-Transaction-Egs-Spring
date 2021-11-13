package com.egs.bank.transaction.spring.entity;

import com.egs.bank.transaction.spring.enums.TransactionStatus;
import com.egs.bank.transaction.spring.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus = TransactionStatus.NEW;

    @Column(name = "created_data")
    private LocalDateTime createdData;

    @Column(name = "transaction_amount")
    private Long transactionAmount = 0L;

    @ManyToOne()
    @JoinColumn(name = "bank_account_id")
    private BankAccounts bankAccount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public Transactions(
            TransactionType transactionType, TransactionStatus transactionStatus,
            LocalDateTime createdData, Long transactionAmount, BankAccounts bankAccount
    ) {
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.createdData = createdData;
        this.transactionAmount = transactionAmount;
        this.bankAccount = bankAccount;
    }
}
