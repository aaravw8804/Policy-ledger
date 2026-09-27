package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.Claim;
import com.hdfclife.ledger.domain.Customer;
import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.dto.ClaimResponse;
import com.hdfclife.ledger.dto.CreatePolicyRequest;
import com.hdfclife.ledger.dto.PolicyResponse;
import com.hdfclife.ledger.exception.DuplicatePolicyException;
import com.hdfclife.ledger.exception.InvalidRequestException;
import com.hdfclife.ledger.exception.PolicyNotFoundException;
import com.hdfclife.ledger.repo.ClaimRepository;
import com.hdfclife.ledger.repo.CustomerRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final ClaimRepository claimRepository;

    public PolicyService(PolicyRepository policyRepository,
                          CustomerRepository customerRepository,
                          ClaimRepository claimRepository) {
        this.policyRepository = policyRepository;
        this.customerRepository = customerRepository;
        this.claimRepository = claimRepository;
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> getAllPolicies() {
        return policyRepository.findAll().stream()
                .sorted(Comparator.comparing(Policy::getPolicyNo))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PolicyResponse getPolicyByNo(String policyNo) {
        return toResponse(findPolicyOrThrow(policyNo));
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> getByStatus(String status) {
        return policyRepository.findByStatusOrderByPolicyNoAsc(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> getByType(String type) {
        return policyRepository.findByProductTypeOrderByPolicyNoAsc(type).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> getByCustomer(String fullName) {
        return policyRepository.findByCustomer_FullNameOrderByPolicyNoAsc(fullName).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> searchByMinPremium(Integer minPremium) {
        if (minPremium == null || minPremium < 0) {
            throw new InvalidRequestException("minPremium must be a non-negative number");
        }
        return policyRepository.findWithPremiumAtLeast(minPremium).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PolicyResponse createPolicy(CreatePolicyRequest request) {
        if (policyRepository.existsByPolicyNo(request.getPolicyNo())) {
            throw new DuplicatePolicyException(request.getPolicyNo());
        }

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseGet(() -> customerRepository.save(
                        new Customer(request.getCustomer(), request.getEmail())));

        Policy policy = new Policy(
                request.getPolicyNo(),
                customer,
                request.getType(),
                request.getBasePremium(),
                request.getStatus());

        return toResponse(policyRepository.save(policy));
    }

    @Transactional
    public void deletePolicy(String policyNo) {
        Policy policy = findPolicyOrThrow(policyNo);
        policyRepository.delete(policy);
    }

    @Transactional(readOnly = true)
    public List<ClaimResponse> getClaimsForPolicy(String policyNo) {
        findPolicyOrThrow(policyNo);
        return claimRepository.findByPolicy_PolicyNoOrderByClaimNoAsc(policyNo).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Policy findPolicyOrThrow(String policyNo) {
        return policyRepository.findByPolicyNo(policyNo)
                .orElseThrow(() -> new PolicyNotFoundException(policyNo));
    }

    private PolicyResponse toResponse(Policy policy) {
        List<String> riderCodes = policy.getRiders().stream()
                .map(r -> r.getCode())
                .sorted()
                .collect(Collectors.toList());

        return new PolicyResponse(
                policy.getPolicyNo(),
                policy.getCustomer().getFullName(),
                policy.getCustomer().getEmail(),
                policy.getProductType(),
                policy.getBasePremium(),
                policy.getStatus(),
                riderCodes);
    }

    private ClaimResponse toResponse(Claim claim) {
        return new ClaimResponse(
                claim.getClaimNo(),
                claim.getPolicy().getPolicyNo(),
                claim.getAmount(),
                claim.getUrgency(),
                claim.getStatus());
    }
}
