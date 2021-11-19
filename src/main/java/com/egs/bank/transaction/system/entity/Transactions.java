package com.egs.bank.transaction.system.entity;

import com.egs.bank.transaction.system.enums.TransactionStatus;
import com.egs.bank.transaction.system.enums.TransactionType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Getter
@Setter
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

    public Transactions(TransactionType transactionType, TransactionStatus transactionStatus,
                        LocalDateTime createdData, Long transactionAmount, BankAccounts bankAccount) {
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.createdData = createdData;
        this.transactionAmount = transactionAmount;
        this.bankAccount = bankAccount;
    }
}
