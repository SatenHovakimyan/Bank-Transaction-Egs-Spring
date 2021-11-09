package com.egs.bank.transaction.spring.enums;

import java.util.List;

public enum TransactionStatus {
    NEW(1) {
        public String toString() {
            return "id: 1, status: NEW";
        }
    },

    PENDING(2) {
        public String toString() {
            return "id: 2, type: PENDING";
        }
    },

    ACCEPTED(3) {
        public String toString() {
            return "id: 3, type: ACCEPTED";
        }
    },

    REJECTED(4) {
        public String toString() {
            return "id: 4, type: REJECTED";
        }
    },

    CLOSED(5) {
        public String toString() {
            return "id: 5, type: CLOSED";
        }
    };

    private final int id;

    TransactionStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TransactionStatus getById(int id) {
        List<TransactionStatus> statuses = List.of(TransactionStatus.values());
        for (TransactionStatus status :
                statuses) {
            if (status.id == id) {
                return status;
            }
        }
        return null;
    }
}
