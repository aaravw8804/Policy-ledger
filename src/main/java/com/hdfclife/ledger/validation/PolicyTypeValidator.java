package com.hdfclife.ledger.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class PolicyTypeValidator implements ConstraintValidator<PolicyType, String> {

    private static final Set<String> ALLOWED = Set.of("TERM", "ULIP", "ENDOWMENT");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return ALLOWED.contains(value);
    }
}
