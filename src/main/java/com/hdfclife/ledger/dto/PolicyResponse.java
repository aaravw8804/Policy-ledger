package com.hdfclife.ledger.dto;

import java.util.List;

public class PolicyResponse {

    private String policyNo;
    private String customer;
    private String email;
    private String type;
    private int basePremium;
    private String status;
    private List<String> riderCodes;

    public PolicyResponse(String policyNo, String customer, String email, String type,
                           int basePremium, String status, List<String> riderCodes) {
        this.policyNo = policyNo;
        this.customer = customer;
        this.email = email;
        this.type = type;
        this.basePremium = basePremium;
        this.status = status;
        this.riderCodes = riderCodes;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public String getCustomer() {
        return customer;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public int getBasePremium() {
        return basePremium;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getRiderCodes() {
        return riderCodes;
    }
}
