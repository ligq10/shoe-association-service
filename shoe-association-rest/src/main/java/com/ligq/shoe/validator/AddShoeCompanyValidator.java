package com.ligq.shoe.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddShoeCompanyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return AddShoeCompanyValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ValidationUtils.rejectIfEmpty(errors, "name", "company.name.empty");
		ValidationUtils.rejectIfEmpty(errors, "address", "company.address.empty");
		ValidationUtils.rejectIfEmpty(errors, "enterpriseLegalPerson", "company.enterpriselegalperson.empty");
		ValidationUtils.rejectIfEmpty(errors, "submitPerson", "submitperson.empty");
		ValidationUtils.rejectIfEmpty(errors, "tel", "tel.empty");
		ValidationUtils.rejectIfEmpty(errors, "logoImageId", "company.logoimageid.empty");
		ValidationUtils.rejectIfEmpty(errors, "permitImageId", "company.permitimageid.empty");

	}

}
