package com.hdfclife.ledger.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_no", nullable = false, unique = true)
    private String policyNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "product_type", nullable = false)
    private String productType;

    @Column(name = "base_premium", nullable = false)
    private int basePremium;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "policy", fetch = FetchType.LAZY)
    private List<Claim> claims = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "policy_riders",
            joinColumns = @JoinColumn(name = "policy_id"),
            inverseJoinColumns = @JoinColumn(name = "rider_id")
    )
    private Set<Rider> riders = new HashSet<>();

    protected Policy() {
    }

    public Policy(String policyNo, Customer customer, String productType, int basePremium, String status) {
        this.policyNo = policyNo;
        this.customer = customer;
        this.productType = productType;
        this.basePremium = basePremium;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getBasePremium() {
        return basePremium;
    }

    public void setBasePremium(int basePremium) {
        this.basePremium = basePremium;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public Set<Rider> getRiders() {
        return riders;
    }

    public void addRider(Rider rider) {
        this.riders.add(rider);
    }
}
