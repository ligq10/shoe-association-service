package com.ligq.shoe.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ligq.shoe.constants.AuditResult;
import com.ligq.shoe.constants.AuditType;
import com.ligq.shoe.constants.FeedbackAuditStatus;
import com.ligq.shoe.constants.ScoreType;
import com.ligq.shoe.constants.ShoeCompanyAuditStatus;
import com.ligq.shoe.controller.AuditMessageController;
import com.ligq.shoe.controller.FeedbackController;
import com.ligq.shoe.controller.SendMsgController;
import com.ligq.shoe.entity.AuditMessage;
import com.ligq.shoe.entity.FeedbackScore;
import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.model.AuditMessageAddRequest;
import com.ligq.shoe.model.AuditMessageResponse;
import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.repository.AuditMessageRepository;
import com.ligq.shoe.repository.FeedbackScoreRepository;
import com.ligq.shoe.repository.ShoeCompanyRepository;
import com.ligq.shoe.utils.BeanUtils;

@Service
public class AuditMessageService {
	private final static Logger logger = LoggerFactory.getLogger(AuditMessageService.class); 

	@Autowired
	private AuditMessageRepository auditMessageRepository;

	@Autowired
	private FeedbackScoreRepository feedbackScoreRepository;
	
	@Autowired
	private ShoeCompanyRepository shoeCompanyRepository;
	
	@Autowired
	private SendMsgService sendMsgService;
	
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
			this.updateCompanyStatus(auditMessage,request);
		}else if(auditMessage.getBusinessType().equalsIgnoreCase(AuditType.FEEDBACK.getValue())){
			this.updateFeedbackStatus(auditMessage);
		}
		
		HttpHeaders headers = new HttpHeaders();

		URI selfUrl = linkTo(methodOn(AuditMessageController.class).findOneAuditById(auditMessage.getUuid(), request, response)).toUri();
		headers.setLocation(selfUrl);
		return new ResponseEntity<Object>(headers,HttpStatus.CREATED);

	}

	private void updateFeedbackStatus(AuditMessage auditMessage) {
		FeedbackScore feedbackScore = feedbackScoreRepository.findOne(auditMessage.getBusinessId());
		if(null != feedbackScore){
			if(feedbackScore.getApproveStatus() == FeedbackAuditStatus.WAITING_AUDIT.getValue()){
				if(auditMessage.getAuditResult().equalsIgnoreCase(AuditResult.PASS.getValue())){
					feedbackScore.setApproveStatus(FeedbackAuditStatus.PRIMARY_PASS_AUDIT.getValue());
					feedbackScoreRepository.save(feedbackScore);
				}else{
					feedbackScore.setApproveStatus(FeedbackAuditStatus.PRIMARY_REFUSE_AUDIT.getValue());
					feedbackScoreRepository.save(feedbackScore);
				}
			}else if(feedbackScore.getApproveStatus() == FeedbackAuditStatus.PRIMARY_PASS_AUDIT.getValue()){
				if(auditMessage.getAuditResult().equalsIgnoreCase(AuditResult.PASS.getValue())){
					ShoeCompany shoeCompany = shoeCompanyRepository.findOne(feedbackScore.getCompanyId());

					feedbackScore.setApproveStatus(FeedbackAuditStatus.MIDDLE_PASS_AUDIT.getValue());
/*					Integer score = 0;
					if(null != shoeCompany){
						if(feedbackScore.getScoreType().equalsIgnoreCase(ScoreType.PLUS.getValue())){
							score = shoeCompany.getServeScore() + feedbackScore.getScore();
						}else{
							score = shoeCompany.getServeScore() - feedbackScore.getScore();						
						}
					}*/

					feedbackScoreRepository.save(feedbackScore);
				}else{
					feedbackScore.setApproveStatus(FeedbackAuditStatus.MIDDLE_REFUSE_AUDIT.getValue());
					feedbackScoreRepository.save(feedbackScore);
				}				
			}
		}
	}

	private void updateCompanyStatus(AuditMessage auditMessage,HttpServletRequest request) {
		ShoeCompany shoeCompany = shoeCompanyRepository.findOne(auditMessage.getBusinessId());
		if(null != shoeCompany){
			if(shoeCompany.getAuditStatus() == ShoeCompanyAuditStatus.WAITING_AUDIT.getValue()){
				if(auditMessage.getAuditResult().equalsIgnoreCase(AuditResult.PASS.getValue())){
					shoeCompany.setAuditStatus(ShoeCompanyAuditStatus.PASS_AUDIT.getValue());
					shoeCompanyRepository.save(shoeCompany);
					SendMsg sendMsg =new SendMsg();
					sendMsg.setMobile(shoeCompany.getTel());
					sendMsg.setContent("中国鞋材网提醒你:尊敬的"+shoeCompany.getSubmitPerson()+",你所添加的"+shoeCompany.getName()+",已经通过审核。");
					ResponseEntity<?> responseEntity = sendMsgService.sendCheckMsg(sendMsg, request);
					HttpStatus responseStatus = responseEntity.getStatusCode();
					if(responseStatus.equals(HttpStatus.OK)){
						logger.info("发送号码:"+sendMsg.getMobile()+"发送内容:"+sendMsg.getContent());				
					}
				}else{
					shoeCompany.setAuditStatus(ShoeCompanyAuditStatus.REFUSE_AUDIT.getValue());
					shoeCompanyRepository.save(shoeCompany);					
					SendMsg sendMsg =new SendMsg();
					sendMsg.setMobile(shoeCompany.getTel());
					sendMsg.setContent("中国鞋材网提醒你:尊敬的"+shoeCompany.getSubmitPerson()+",你所添加的"+shoeCompany.getName()+",审核未通过,原因是:"+auditMessage.getAuditRemark());
					ResponseEntity<?> responseEntity = sendMsgService.sendCheckMsg(sendMsg, request);
					HttpStatus responseStatus = responseEntity.getStatusCode();
					if(responseStatus.equals(HttpStatus.OK)){
						logger.info("发送号码:"+sendMsg.getMobile()+"发送内容:"+sendMsg.getContent());				
					}				
				}
			}
		}
	}

	public AuditMessageResponse findOneAuditById(String uuid,
			HttpServletRequest request, HttpServletResponse response) {
		AuditMessage auditMessage = auditMessageRepository.findOne(uuid);
		if(null == auditMessage){
			return null;
		}
		AuditMessageResponse auditMessageResponse = new AuditMessageResponse();
		BeanUtils.copyProperties(auditMessage, auditMessageResponse);
		Link selfLink = linkTo(methodOn(AuditMessageController.class).findOneAuditById(auditMessage.getUuid(), request, response)).withSelfRel();
		auditMessageResponse.add(selfLink);;
		return auditMessageResponse;
	}
	
	
}
