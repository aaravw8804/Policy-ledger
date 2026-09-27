package com.hdfclife.ledger.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PolicyTypeValidator.class)
public @interface PolicyType {

    String message() default "must be one of TERM, ULIP, ENDOWMENT";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
