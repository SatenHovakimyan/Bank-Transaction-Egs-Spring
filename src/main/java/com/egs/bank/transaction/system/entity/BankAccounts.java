package com.egs.bank.transaction.system.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "bank_account")
@NoArgsConstructor
@Getter
@Setter
public class BankAccounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "bankAccount")
    private List<Transactions> transactions;

    public BankAccounts(Long balance, LocalDate createdDate, Users user) {
        this.balance = balance;
        this.createdDate = createdDate;
        this.user = user;
    }
}
