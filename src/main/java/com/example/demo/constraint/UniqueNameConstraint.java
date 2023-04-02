package com.example.demo.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = UniqueNameValidator.class)
public @interface UniqueNameConstraint {
    @SuppressWarnings("unused")
    String message() default "${validatedValue.firstName} ${validatedValue.lastName} already exists in the database";

    @SuppressWarnings("unused")
    Class<?>[] groups() default {};

    @SuppressWarnings("unused")
    Class<? extends Payload>[] payload() default {};
}
