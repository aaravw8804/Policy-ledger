package com.hdfclife.ledger.dto;

import java.util.Map;

public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private Map<String, String> fields;

    public ErrorResponse(int status, String error, String message, Map<String, String> fields) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.fields = fields;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getFields() {
        return fields;
    }
}
