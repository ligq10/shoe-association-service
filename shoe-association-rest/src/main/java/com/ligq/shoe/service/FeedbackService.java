package com.ligq.shoe.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ligq.shoe.controller.FeedbackController;
import com.ligq.shoe.entity.FeedbackScore;
import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.model.FeedbackAddRequest;
import com.ligq.shoe.repository.FeedbackFileRepository;
import com.ligq.shoe.repository.FeedbackScoreRepository;

@Service
public class FeedbackService {

	private final static Logger logger = LoggerFactory.getLogger(FeedbackService.class); 

	@Autowired
	private FeedbackScoreRepository feedbackScoreRepository;
	@Autowired
	private FeedbackFileRepository feedbackFileRepository;
	
	public ResponseEntity<Object> save(FeedbackAddRequest feedBackAddRequest,
			HttpServletRequest request, HttpServletResponse response) {

		FeedbackScore feedbackScore = new FeedbackScore();		
		BeanUtils.copyProperties(feedBackAddRequest, feedbackScore);
		feedbackScore.setUuid(UUID.randomUUID().toString());
		feedbackScore.setCreate_time(new Date());
		feedbackScore = feedbackScoreRepository.save(feedbackScore);
		HttpHeaders headers = new HttpHeaders();

		URI selfUrl = linkTo(methodOn(FeedbackController.class).findOneFeedbackById(feedbackScore.getUuid(), request, response)).toUri();
		headers.setLocation(selfUrl);
		return new ResponseEntity<Object>(headers,HttpStatus.CREATED);
	}

	public FeedbackScore findOneFeedbackById(String uuid) {
		// TODO Auto-generated method stub
		FeedbackScore feedbackScore = feedbackScoreRepository.findOne(uuid);
		return feedbackScore;
	}

	public Page<FeedbackScore> findFeedbackByCompanyId(String uuid,
			Pageable pageable) {
		Page<FeedbackScore> feedbackScorePage = feedbackScoreRepository.findByCompanyId(uuid, pageable);
		return feedbackScorePage;
	}
	
	
}
