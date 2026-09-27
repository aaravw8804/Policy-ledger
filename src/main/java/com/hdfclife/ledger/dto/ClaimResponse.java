package com.hdfclife.ledger.dto;

public class ClaimResponse {

    private String claimNo;
    private String policyNo;
    private int claimAmount;
    private String urgency;
    private String status;

    public ClaimResponse(String claimNo, String policyNo, int claimAmount, String urgency, String status) {
        this.claimNo = claimNo;
        this.policyNo = policyNo;
        this.claimAmount = claimAmount;
        this.urgency = urgency;
        this.status = status;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public int getClaimAmount() {
        return claimAmount;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getStatus() {
        return status;
    }
}
