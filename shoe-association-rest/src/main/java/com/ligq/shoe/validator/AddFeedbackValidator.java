package com.ligq.shoe.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddFeedbackValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return AddFeedbackValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ValidationUtils.rejectIfEmpty(errors, "scoreType", "scoretype.empty");
		ValidationUtils.rejectIfEmpty(errors, "score", "score.empty");
		ValidationUtils.rejectIfEmpty(errors, "scoreReason", "scorereason.empty");
		ValidationUtils.rejectIfEmpty(errors, "submitPerson", "submitperson.empty");
		ValidationUtils.rejectIfEmpty(errors, "submitTel", "submittel.empty");

	}

}
