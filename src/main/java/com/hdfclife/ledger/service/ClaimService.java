package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.Claim;
import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.dto.ClaimResponse;
import com.hdfclife.ledger.dto.CreateClaimRequest;
import com.hdfclife.ledger.exception.PolicyNotFoundException;
import com.hdfclife.ledger.repo.ClaimRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;

    public ClaimService(ClaimRepository claimRepository, PolicyRepository policyRepository) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
    }

    @Transactional
    public ClaimResponse createClaim(CreateClaimRequest request) {
        Policy policy = policyRepository.findByPolicyNo(request.getPolicyNo())
                .orElseThrow(() -> new PolicyNotFoundException(request.getPolicyNo()));

        String claimNo = nextClaimNo();

        Claim claim = new Claim(
                claimNo,
                policy,
                request.getClaimAmount(),
                request.getUrgency(),
                "SUBMITTED");

        Claim saved = claimRepository.save(claim);

        return new ClaimResponse(
                saved.getClaimNo(),
                policy.getPolicyNo(),
                saved.getAmount(),
                saved.getUrgency(),
                saved.getStatus());
    }

    private String nextClaimNo() {
        long next = claimRepository.count() + 1;
        return String.format("CLM-%02d", next);
    }
}
