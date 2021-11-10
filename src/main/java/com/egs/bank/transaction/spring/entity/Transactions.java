package com.egs.bank.transaction.spring.entity;

import com.egs.bank.transaction.spring.enums.TransactionStatus;
import com.egs.bank.transaction.spring.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
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
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TransactionType transactionType;

    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "created_data")
    private LocalDate createdData;

    @Column(name = "transaction_sum")
    private Long transactionSum;

    @ManyToOne()
    @JoinColumn(name = "bank_account_id")
    private BankAccounts bankAccount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public Transactions(
            TransactionType transactionType, TransactionStatus transactionStatus,
            LocalDate createdData, Long transactionSum, BankAccounts bankAccount
    ) {
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.createdData = createdData;
        this.transactionSum = transactionSum;
        this.bankAccount = bankAccount;
    }
}
