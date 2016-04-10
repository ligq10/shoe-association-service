package com.ligq.shoe.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.ligq.shoe.constants.CheckCodeType;
import com.ligq.shoe.entity.FeedbackScore;
import com.ligq.shoe.model.FeedbackAddRequest;
import com.ligq.shoe.model.FeedbackResponse;
import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.service.FeedbackService;
import com.ligq.shoe.validator.AddFeedbackValidator;
import com.ligq.shoe.validator.AddFeedbackWithoutAuditValidator;

@Controller
public class FeedbackController {

	private final static Logger logger = LoggerFactory.getLogger(ShoeCompanyController.class); 

	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private AddFeedbackValidator addFeedbackValidator;
	@Autowired
	private AddFeedbackWithoutAuditValidator addFeedbackWithoutAuditValidator;
	
	@RequestMapping(value="/shoecompanies/{uuid}/feedbacks",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> saveFeedback(
			@PathVariable String uuid,
			@RequestBody FeedbackAddRequest feedBackAddRequest,
			HttpServletRequest request,
			HttpServletResponse response,
			BindingResult result){
		addFeedbackValidator.validate(feedBackAddRequest, result);
		if(result.hasErrors()){
			logger.error("Add Feedback validation failed:"+result);
			throw new RepositoryConstraintViolationException(result);
		}
		HttpSession session = request.getSession();
		SendMsg sendMsg = (SendMsg) session.getAttribute(feedBackAddRequest.getSubmitTel());
		if(null == sendMsg){
			return new ResponseEntity<Object>("验证码无效，请检查!",HttpStatus.BAD_REQUEST);			
		}
		String checkCode = sendMsg.getCheckCode();
		if(sendMsg.getType().equals(CheckCodeType.FEEDBACK.getValue()) == false 
				|| checkCode.equals(feedBackAddRequest.getCheckCode()) == false){
			return new ResponseEntity<Object>("验证码错误，请检查!",HttpStatus.BAD_REQUEST);			
		}
		session.removeAttribute(feedBackAddRequest.getSubmitTel());

		feedBackAddRequest.setCompanyId(uuid);
		ResponseEntity<Object> responseEntity =  null;		
		try {	        
	        responseEntity=feedbackService.save(feedBackAddRequest,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage(),e);
			responseEntity=new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;
	}
	
	@RequestMapping(value="/shoecompanies/{uuid}/feedbacks/withoutaudit",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> saveFeedbackWithoutAudit(
			@PathVariable String uuid,
			@RequestBody FeedbackAddRequest feedBackAddRequest,
			HttpServletRequest request,
			HttpServletResponse response,
			BindingResult result){
		
		addFeedbackWithoutAuditValidator.validate(feedBackAddRequest, result);
		if(result.hasErrors()){
			logger.error("Add Feedback validation failed:"+result);
			throw new RepositoryConstraintViolationException(result);
		}

		feedBackAddRequest.setCompanyId(uuid);
		ResponseEntity<Object> responseEntity =  null;		
		try {	        
	        responseEntity=feedbackService.saveFeedbackAndUpdateCompany(feedBackAddRequest,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage(),e);
			responseEntity=new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;
	}
	
	@RequestMapping(value="/feedbacks/{uuid}",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findOneFeedbackById(
			 @PathVariable String uuid,
			 HttpServletRequest request,
			 HttpServletResponse response){
		FeedbackResponse feedbackResponse = feedbackService.findOneFeedbackById(uuid,request,response);
		if(null == feedbackResponse){
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}		
        return new ResponseEntity<Resource>(new Resource<FeedbackResponse>(feedbackResponse), HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/shoecompanies/{uuid}/feedbacks",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findFeedbackByShoeCompany(
			@PathVariable String uuid,
			@RequestParam(value = "auditStatus", required = false, defaultValue = "0") int auditStatus,
            @PageableDefault(page = 0, size = 20, sort = "createTime", direction = Sort.Direction.ASC) Pageable pageable,
			HttpServletRequest request,
			HttpServletResponse response){
		ResponseEntity responseEntity = null;
		Page<FeedbackScore> feedbackScorePage = feedbackService.findFeedbackByCompanyIdAndApproveStatus(uuid,auditStatus,pageable);
		
		try {
			responseEntity = feedbackService.getResponseEntityConvertFeedbackPage("",feedbackScorePage, pageable, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("User Locations Not Found:",e);
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);			
		}
		return  responseEntity;	
		
	}
	
	@RequestMapping(value="/feedbacks",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findAllFeedbacks(
            @PageableDefault(page = 0, size = 20, sort = "createTime", direction = Sort.Direction.ASC) Pageable pageable,
			HttpServletRequest request,
			HttpServletResponse response){
		ResponseEntity responseEntity = null;
		Page<FeedbackScore> feedbackScorePage = feedbackService.findAll(pageable);
		
		try {
			responseEntity = feedbackService.getResponseEntityConvertFeedbackPage("",feedbackScorePage, pageable, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("User Locations Not Found:",e);
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);			
		}
		return  responseEntity;	
		
	}
	
	@RequestMapping(value="/feedbacks/audit",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findAuditFeedbackList(
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,			
			@RequestParam(value = "auditStatus", required = false, defaultValue = "0") int auditStatus,
            @PageableDefault(page = 0, size = 20, sort = "createTime", direction = Sort.Direction.ASC) Pageable pageable,
			HttpServletRequest request,
			HttpServletResponse response){
		
		StringBuilder queryParams = new  StringBuilder();
		if(StringUtils.isEmpty(keyword) == false){
			queryParams.append("&keyword"+keyword);
		}
		if(StringUtils.isEmpty(keyword) == false){
			queryParams.append("&auditStatus"+auditStatus);
		}
		ResponseEntity responseEntity = null;
		Page<FeedbackScore> feedbackScorePage = feedbackService.findFeedbackBySearchKeywordAndAudit(keyword,auditStatus,pageable);		
		try {
			responseEntity = feedbackService.getResponseEntityConvertFeedbackPage(queryParams.toString(),feedbackScorePage, pageable, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("User Locations Not Found:",e);
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);			
		}
		return  responseEntity;	
		
	}
	
	@RequestMapping(value="/feedbacks/{uuid}",method = RequestMethod.DELETE, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> deleteFeedbackById(
			 @PathVariable String uuid,
			 HttpServletRequest request,
			 HttpServletResponse response){

		ResponseEntity<?> responseEntity =  null;		
		try {	        
	        responseEntity=feedbackService.delete(uuid,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage(),e);
			responseEntity=new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;
	}
}
