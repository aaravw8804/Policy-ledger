package com.hdfclife.ledger.dto;

import com.hdfclife.ledger.validation.PolicyType;
import jakarta.validation.constraints.*;

public class CreatePolicyRequest {

    @NotBlank
    @Pattern(regexp = "HDFC-LIFE-[0-9]{4}")
    private String policyNo;

    @NotBlank
    @Size(min = 2, max = 120)
    private String customer;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @PolicyType
    private String type;

    @NotNull
    @Min(1)
    private Integer basePremium;

    @NotBlank
    @Pattern(regexp = "Active|Lapsed|Pending")
    private String status;

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getBasePremium() {
        return basePremium;
    }

    public void setBasePremium(Integer basePremium) {
        this.basePremium = basePremium;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
