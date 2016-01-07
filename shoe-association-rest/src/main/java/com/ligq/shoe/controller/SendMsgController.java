package com.ligq.shoe.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.service.SendMsgService;

@Controller
public class SendMsgController {
	
	@Autowired
	private SendMsgService sendMsgService;
	
	@RequestMapping(value="/sendcheckcode",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> saveShoeCompany(
			@RequestBody SendMsg sendMsg,
			HttpServletRequest request,
			HttpServletResponse response){
		ResponseEntity<Object> responseEntity =  null;	
		sendMsgService.sendCheckMsg(sendMsg);
		responseEntity=new ResponseEntity<Object>(HttpStatus.OK);			

		return responseEntity;
	}
}
