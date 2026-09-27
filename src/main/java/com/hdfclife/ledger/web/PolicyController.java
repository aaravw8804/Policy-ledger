package com.hdfclife.ledger.web;

import com.hdfclife.ledger.dto.ClaimResponse;
import com.hdfclife.ledger.dto.CreatePolicyRequest;
import com.hdfclife.ledger.dto.PolicyResponse;
import com.hdfclife.ledger.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
@Tag(name = "Policies", description = "HDFC Life policy operations")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    @Operation(summary = "List policies, optionally filtered by status, type or customer")
    @ApiResponse(responseCode = "200", description = "Policies returned")
    public List<PolicyResponse> getPolicies(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String customer) {

        if (status != null) {
            return policyService.getByStatus(status);
        }
        if (type != null) {
            return policyService.getByType(type);
        }
        if (customer != null) {
            return policyService.getByCustomer(customer);
        }
        return policyService.getAllPolicies();
    }

    @GetMapping("/search")
    @Operation(summary = "Search policies with base premium at or above minPremium")
    @ApiResponse(responseCode = "200", description = "Policies returned")
    @ApiResponse(responseCode = "400", description = "minPremium missing or negative")
    public List<PolicyResponse> searchPolicies(@RequestParam(required = false) Integer minPremium) {
        return policyService.searchByMinPremium(minPremium);
    }

    @GetMapping("/{policyNo}")
    @Operation(summary = "Get a policy by policy number")
    @ApiResponse(responseCode = "200", description = "Policy found")
    @ApiResponse(responseCode = "404", description = "Policy not found")
    public PolicyResponse getPolicy(@PathVariable String policyNo) {
        return policyService.getPolicyByNo(policyNo);
    }

    @GetMapping("/{policyNo}/claims")
    @Operation(summary = "List claims for a policy")
    @ApiResponse(responseCode = "200", description = "Claims returned")
    @ApiResponse(responseCode = "404", description = "Policy not found")
    public List<ClaimResponse> getClaimsForPolicy(@PathVariable String policyNo) {
        return policyService.getClaimsForPolicy(policyNo);
    }

    @PostMapping
    @Operation(summary = "Create a policy, creating the customer if the email is new")
    @ApiResponse(responseCode = "201", description = "Policy created")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "409", description = "Duplicate policy number")
    public ResponseEntity<PolicyResponse> createPolicy(@Valid @RequestBody CreatePolicyRequest request) {
        PolicyResponse created = policyService.createPolicy(request);
        return ResponseEntity.created(URI.create("/api/policies/" + created.getPolicyNo())).body(created);
    }

    @DeleteMapping("/{policyNo}")
    @Operation(summary = "Delete a policy")
    @ApiResponse(responseCode = "204", description = "Policy deleted")
    @ApiResponse(responseCode = "404", description = "Policy not found")
    public ResponseEntity<Void> deletePolicy(@PathVariable String policyNo) {
        policyService.deletePolicy(policyNo);
        return ResponseEntity.noContent().build();
    }
}
