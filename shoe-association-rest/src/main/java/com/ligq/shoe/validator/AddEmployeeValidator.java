package com.ligq.shoe.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddEmployeeValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		return AddEmployeeValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ValidationUtils.rejectIfEmpty(errors, "name", "employeeName.empty");
		ValidationUtils.rejectIfEmpty(errors, "loginName", "loginName.empty");
		ValidationUtils.rejectIfEmpty(errors, "password", "password.empty");

	}

}
