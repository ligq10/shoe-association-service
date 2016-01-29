package com.ligq.shoe.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ligq.shoe.model.AuditMessageAddRequest;
import com.ligq.shoe.model.AuditMessageResponse;
import com.ligq.shoe.service.AuditMessageService;
import com.ligq.shoe.validator.AddAuditMessageValidator;

@Controller
public class AuditMessageController {

	private final static Logger logger = LoggerFactory.getLogger(AuditMessageController.class); 

	@Autowired
	private AuditMessageService auditMessageService;
	
	@Autowired
	private AddAuditMessageValidator addAuditMessageValidator;
	
	@RequestMapping(value="/audits",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> saveAudit(
			@RequestBody AuditMessageAddRequest auditMessageAddRequest,
			HttpServletRequest request,
			HttpServletResponse response,
			BindingResult result){
		addAuditMessageValidator.validate(auditMessageAddRequest, result);
		if(result.hasErrors()){
			logger.error("Add Audit validation failed:"+result);
			throw new RepositoryConstraintViolationException(result);
		}

		ResponseEntity<Object> responseEntity =  null;		
		try {	        
	        responseEntity=auditMessageService.save(auditMessageAddRequest,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage(),e);
			responseEntity=new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;
	}
	
	@RequestMapping(value="/audits/{uuid}",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findOneAuditById(
			 @PathVariable String uuid,
			 HttpServletRequest request,
			 HttpServletResponse response){
		AuditMessageResponse auditMessageResponse = auditMessageService.findOneAuditById(uuid,request,response);
		if(null == auditMessageResponse){
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}		
        return new ResponseEntity<Resource>(new Resource<AuditMessageResponse>(auditMessageResponse), HttpStatus.OK);
		
	}

}
