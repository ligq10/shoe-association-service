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
import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.model.FeedbackAddRequest;
import com.ligq.shoe.model.FeedbackResponse;
import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.model.ShoeCompanyAddRequest;
import com.ligq.shoe.model.ShoeCompanyResponse;
import com.ligq.shoe.service.FeedbackService;
import com.ligq.shoe.service.ShoeCompanyService;
import com.ligq.shoe.validator.AddFeedbackValidator;

@Controller
public class FeedbackController {

	private final static Logger logger = LoggerFactory.getLogger(ShoeCompanyController.class); 

	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private AddFeedbackValidator addFeedbackValidator;
	
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
		String checkCode = sendMsg.getContent();
		if(sendMsg.getType().equals(CheckCodeType.REGISTER.getValue()) == false 
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
	public HttpEntity<?> findShoeCompany(
			@PathVariable String uuid,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "20") int size,
			HttpServletRequest request,
			HttpServletResponse response){
		ResponseEntity responseEntity = null;
		Pageable pageable = new PageRequest(page, size);
		Page<FeedbackScore> feedbackScorePage = feedbackService.findFeedbackByCompanyId(uuid,pageable);
		
		try {
			responseEntity = feedbackService.getResponseEntityConvertFeedbackPage("",feedbackScorePage, pageable, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("User Locations Not Found:",e);
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);			
		}
		return  responseEntity;	
		
	}
}
