package com.ligq.shoe.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

import com.ligq.shoe.entity.FeedbackScore;
import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.model.FeedbackAddRequest;
import com.ligq.shoe.model.FeedbackResponse;
import com.ligq.shoe.model.ShoeCompanyAddRequest;
import com.ligq.shoe.model.ShoeCompanyResponse;
import com.ligq.shoe.service.FeedbackService;
import com.ligq.shoe.service.ShoeCompanyService;

@Controller
public class FeedbackController {

	private final static Logger logger = LoggerFactory.getLogger(ShoeCompanyController.class); 

	@Autowired
	private FeedbackService feedbackService;
	
	@RequestMapping(value="/shoecompanies/{uuid}/feedbacks",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> saveFeedback(
			@PathVariable String uuid,
			@RequestBody FeedbackAddRequest feedBackAddRequest,
			HttpServletRequest request,
			HttpServletResponse response,
			BindingResult result){
		
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
		FeedbackScore feedbackScore = feedbackService.findOneFeedbackById(uuid);
		if(null == feedbackScore){
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		FeedbackResponse feedbackResponse = new FeedbackResponse();
		BeanUtils.copyProperties(feedbackScore, feedbackResponse);
	    Link selfLink = linkTo(methodOn(this.getClass()).findOneFeedbackById(feedbackScore.getUuid(), request, response)).withSelfRel();	    
	    feedbackResponse.add(selfLink);
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

		Pageable pageable = new PageRequest(page, size);
		Page<FeedbackScore> feedbackScorePage = feedbackService.findFeedbackByCompanyId(uuid,pageable);
				
        return new ResponseEntity<>(feedbackScorePage, HttpStatus.OK);
	}
}