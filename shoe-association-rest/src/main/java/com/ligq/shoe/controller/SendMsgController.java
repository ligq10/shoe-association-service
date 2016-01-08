package com.ligq.shoe.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.service.SendMsgService;
import com.ligq.shoe.validator.AddSendMsgValidator;

@Controller
public class SendMsgController {
	
	private final static Logger logger = LoggerFactory.getLogger(SendMsgController.class); 
	
	@Autowired
	private SendMsgService sendMsgService;
	@Autowired
	private AddSendMsgValidator addSendMsgValidator;
	
	@RequestMapping(value="/sendcheckcode",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> saveShoeCompany(
			@RequestBody SendMsg sendMsg,
			HttpServletRequest request,
			HttpServletResponse response,
			BindingResult result){
		
		addSendMsgValidator.validate(sendMsg, result);
		if(result.hasErrors()){
			logger.error("Send Check Code validation failed:"+result);
			throw new RepositoryConstraintViolationException(result);
		}
		ResponseEntity<?> responseEntity =  null;	
		try {
			responseEntity = sendMsgService.sendCheckMsg(sendMsg,request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<String>("验证码发送失败",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}
}
