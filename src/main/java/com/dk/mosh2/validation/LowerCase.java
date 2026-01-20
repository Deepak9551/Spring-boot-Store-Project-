package com.dk.mosh2.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = LowerCaseValidator.class)
public @interface LowerCase {
	String message() default "must be lowercase";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
