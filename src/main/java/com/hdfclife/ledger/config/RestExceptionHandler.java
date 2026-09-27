package com.hdfclife.ledger.config;

import com.hdfclife.ledger.dto.ErrorResponse;
import com.hdfclife.ledger.exception.DuplicatePolicyException;
import com.hdfclife.ledger.exception.InvalidRequestException;
import com.hdfclife.ledger.exception.ClaimNotFoundException;
import com.hdfclife.ledger.exception.PolicyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                fields.put(fieldError.getField(), fieldError.getDefaultMessage()));

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                fields);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(PolicyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePolicyNotFound(PolicyNotFoundException ex) {
        return notFound(ex.getMessage());
    }

    @ExceptionHandler(ClaimNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClaimNotFound(ClaimNotFoundException ex) {
        return notFound(ex.getMessage());
    }

    @ExceptionHandler(DuplicatePolicyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatePolicy(DuplicatePolicyException ex) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                Map.of());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(InvalidRequestException ex) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                Map.of());

        return ResponseEntity.badRequest().body(body);
    }

    private ResponseEntity<ErrorResponse> notFound(String message) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                message,
                Map.of());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
