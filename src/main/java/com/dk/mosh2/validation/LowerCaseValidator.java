package com.dk.mosh2.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LowerCaseValidator implements ConstraintValidator<LowerCase, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		if(value == null) return true;
		return value.equals(value.toLowerCase());
	}

}
