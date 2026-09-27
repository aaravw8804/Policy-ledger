package com.hdfclife.ledger.exception;

public abstract class DeskException extends RuntimeException {

    protected DeskException(String message) {
        super(message);
    }
}
