package com.ligq.shoe.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ligq.shoe.constants.AuditType;
import com.ligq.shoe.controller.AuditMessageController;
import com.ligq.shoe.controller.FeedbackController;
import com.ligq.shoe.entity.AuditMessage;
import com.ligq.shoe.model.AuditMessageAddRequest;
import com.ligq.shoe.model.AuditMessageResponse;
import com.ligq.shoe.repository.AuditMessageRepository;
import com.ligq.shoe.utils.BeanUtils;

@Service
public class AuditMessageService {

	@Autowired
	private AuditMessageRepository auditMessageRepository;

	public ResponseEntity<Object> save(
			AuditMessageAddRequest auditMessageAddRequest,
			HttpServletRequest request, HttpServletResponse response) {

		Date createTime = new Date();
		UUID uuid = UUID.randomUUID();
		AuditMessage auditMessage = new AuditMessage();
		BeanUtils.copyPropertiesIgnoreNull(auditMessageAddRequest, auditMessage);
		auditMessage.setUuid(uuid.toString());
		auditMessage.setCreateTime(createTime);
		auditMessageRepository.save(auditMessage);
		
		if(auditMessage.getBusinessType().equalsIgnoreCase(AuditType.REGISTER.getValue())){
			this.updateCompanyStatus(auditMessage);
		}else if(auditMessage.getBusinessType().equalsIgnoreCase(AuditType.FEEDBACK.getValue())){
			this.updateFeedbackStatus(auditMessage);
		}
		
		HttpHeaders headers = new HttpHeaders();

		URI selfUrl = linkTo(methodOn(AuditMessageController.class).findOneAuditById(auditMessage.getUuid(), request, response)).toUri();
		headers.setLocation(selfUrl);
		return new ResponseEntity<Object>(headers,HttpStatus.CREATED);

	}

	private void updateFeedbackStatus(AuditMessage auditMessage) {
		// TODO Auto-generated method stub
		
	}

	private void updateCompanyStatus(AuditMessage auditMessage) {
		// TODO Auto-generated method stub
		
	}

	public AuditMessageResponse findOneAuditById(String uuid,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
