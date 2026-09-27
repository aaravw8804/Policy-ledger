package com.hdfclife.ledger.web;

import com.hdfclife.ledger.dto.ClaimResponse;
import com.hdfclife.ledger.dto.CreateClaimRequest;
import com.hdfclife.ledger.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/claims")
@Tag(name = "Claims", description = "HDFC Life claim operations")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    @Operation(summary = "File a claim against a policy")
    @ApiResponse(responseCode = "201", description = "Claim created")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "404", description = "Policy not found")
    public ResponseEntity<ClaimResponse> createClaim(@Valid @RequestBody CreateClaimRequest request) {
        ClaimResponse created = claimService.createClaim(request);
        return ResponseEntity.created(URI.create("/api/claims/" + created.getClaimNo())).body(created);
    }
}
