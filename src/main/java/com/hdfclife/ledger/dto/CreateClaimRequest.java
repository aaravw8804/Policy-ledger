package com.hdfclife.ledger.dto;

import jakarta.validation.constraints.*;

public class CreateClaimRequest {

    @NotBlank
    @Pattern(regexp = "HDFC-LIFE-[0-9]{4}")
    private String policyNo;

    @NotNull
    @Min(1)
    @Max(500000)
    private Integer claimAmount;

    @NotBlank
    @Pattern(regexp = "HIGH|MEDIUM|LOW")
    private String urgency;

    @Size(max = 120)
    private String hospitalName;

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public Integer getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(Integer claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
