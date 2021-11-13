package com.egs.bank.transaction.spring.enums;

import java.util.List;

public enum TransactionType {

    DEPOSIT(1) {
        public String toString() {
            return "id: 1, type: DEPOSIT";
        }
    },
    WITHDRAWAL(2) {
        public String toString() {
            return "id: 2, type: WITHDRAWAL";
        }
    };
    private final int id;

    TransactionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TransactionType getById(int id) {
        List<TransactionType> types = List.of(TransactionType.values());
        for (TransactionType type :
                types) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
