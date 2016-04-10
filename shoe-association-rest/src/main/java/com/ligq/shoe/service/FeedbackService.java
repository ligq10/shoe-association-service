package com.ligq.shoe.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ligq.shoe.constants.CreditLevel;
import com.ligq.shoe.constants.FeedbackAuditStatus;
import com.ligq.shoe.constants.ScoreItems;
import com.ligq.shoe.constants.ScoreType;
import com.ligq.shoe.controller.FeedbackController;
import com.ligq.shoe.entity.FeedbackFile;
import com.ligq.shoe.entity.FeedbackScore;
import com.ligq.shoe.entity.Image;
import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.model.FeedbackAddRequest;
import com.ligq.shoe.model.FeedbackResponse;
import com.ligq.shoe.repository.FeedbackFileRepository;
import com.ligq.shoe.repository.FeedbackScoreRepository;
import com.ligq.shoe.repository.ShoeCompanyRepository;

@Service
public class FeedbackService {

	private final static Logger logger = LoggerFactory.getLogger(FeedbackService.class); 

	@Autowired
	private FeedbackScoreRepository feedbackScoreRepository;
	
	@Autowired
	private FeedbackFileRepository feedbackFileRepository;
	
	@Autowired
	private ShoeCompanyRepository shoeCompanyRepository;
	
	public ResponseEntity<Object> save(FeedbackAddRequest feedBackAddRequest,
			HttpServletRequest request, HttpServletResponse response) {

		List<String> proofFileIds = feedBackAddRequest.getProofFileIds();
		if(null == proofFileIds || proofFileIds.isEmpty()){
			logger.error("proof file is empty");
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}		
		Date createTime = new Date();
		FeedbackScore feedbackScore = new FeedbackScore();		
		BeanUtils.copyProperties(feedBackAddRequest, feedbackScore);
		feedbackScore.setUuid(UUID.randomUUID().toString());
		feedbackScore.setCreateTime(createTime);
		//0代表初始化状态
		feedbackScore.setApproveStatus(0);
		feedbackScore = feedbackScoreRepository.save(feedbackScore);
		for(String proofFileId : proofFileIds){
			FeedbackFile feedbackFile = new FeedbackFile();
			feedbackFile.setUuid(UUID.randomUUID().toString());
			feedbackFile.setFileId(proofFileId);
			feedbackFile.setFeedbackId(feedbackScore.getUuid());
			feedbackFile.setCreateTime(createTime);
			feedbackFileRepository.save(feedbackFile);			
		}
		
		HttpHeaders headers = new HttpHeaders();

		URI selfUrl = linkTo(methodOn(FeedbackController.class).findOneFeedbackById(feedbackScore.getUuid(), request, response)).toUri();
		headers.setLocation(selfUrl);
		return new ResponseEntity<Object>(headers,HttpStatus.CREATED);
	}

	public FeedbackScore saveFeedbackScore(FeedbackAddRequest feedBackAddRequest,
			HttpServletRequest request, HttpServletResponse response){
		List<String> proofFileIds = feedBackAddRequest.getProofFileIds();
		if(null == proofFileIds || proofFileIds.isEmpty()){
			return null;
		}		
		Date createTime = new Date();
		FeedbackScore feedbackScore = new FeedbackScore();		
		BeanUtils.copyProperties(feedBackAddRequest, feedbackScore);
		feedbackScore.setUuid(UUID.randomUUID().toString());
		feedbackScore.setCreateTime(createTime);
		//0代表初始化状态
		feedbackScore.setApproveStatus(FeedbackAuditStatus.MIDDLE_PASS_AUDIT.getValue());
		feedbackScore = feedbackScoreRepository.save(feedbackScore);
		for(String proofFileId : proofFileIds){
			FeedbackFile feedbackFile = new FeedbackFile();
			feedbackFile.setUuid(UUID.randomUUID().toString());
			feedbackFile.setFileId(proofFileId);
			feedbackFile.setFeedbackId(feedbackScore.getUuid());
			feedbackFile.setCreateTime(createTime);
			feedbackFileRepository.save(feedbackFile);			
		}
		return feedbackScore;
	}
	
	public FeedbackResponse findOneFeedbackById(
			String uuid,
			HttpServletRequest request,
			HttpServletResponse response) {
		FeedbackResponse  feedbackResponse = new FeedbackResponse();
		FeedbackScore feedbackScore = feedbackScoreRepository.findOne(uuid);
		if(null == feedbackScore ){
			return null;
		}
		feedbackResponse = this.getFeedbackResponseByFeedbackScore(feedbackScore, request, response);
		return feedbackResponse;
	}

	public Page<FeedbackScore> findFeedbackByCompanyIdAndApproveStatus(String uuid,Integer auditStatus,
			Pageable pageable) {
		Page<FeedbackScore> feedbackScorePage = feedbackScoreRepository.findByCompanyIdAndApproveStatus(uuid,auditStatus,pageable);
		return feedbackScorePage;
	}

	public Page<FeedbackScore> findAll(
			Pageable pageable) {
		Page<FeedbackScore> feedbackScorePage = feedbackScoreRepository.findAll(pageable);
		return feedbackScorePage;
	}
	
	public ResponseEntity<?> getResponseEntityConvertFeedbackPage(String pathParams,
			Page<FeedbackScore> feedbackScorePage, Pageable pageable,
			HttpServletRequest request, HttpServletResponse response) {
		
		List<Link> list = prepareLinks(pageable.getPageNumber(),
				pageable.getPageSize(), request, feedbackScorePage,pathParams);
		List<FeedbackResponse> content = new ArrayList<FeedbackResponse>();
		
		if(null != feedbackScorePage){
			for (FeedbackScore feedbackScore : feedbackScorePage.getContent()) {
				FeedbackResponse feedbackResponse = getFeedbackResponseByFeedbackScore(feedbackScore,request,response);
				content.add(feedbackResponse);
			}			
		}
		
		PagedResources<FeedbackResponse> pagedResources = new PagedResources<FeedbackResponse>(
				content, new PageMetadata(feedbackScorePage.getSize(), feedbackScorePage.getNumber(),
						feedbackScorePage.getTotalElements(), feedbackScorePage.getTotalPages()),
				list);
		return new ResponseEntity(pagedResources, HttpStatus.OK); 

	}
	
	private List<Link> prepareLinks(int page, int size,
			HttpServletRequest request, Page result,String pathParams) {
		List<Link> list = new ArrayList<>();
		if (result.hasNext()) {
			list.add(new Link(getHost(request) + request.getRequestURI()
					+  "?page=" + (page + 1) + "&size=" + size+pathParams,
					Link.REL_NEXT));
		}
		if (result.hasPrevious()) {
			list.add(new Link(getHost(request) + request.getRequestURI()
					+ "?page=" + (page - 1) + "&size=" + size+pathParams,
					Link.REL_PREVIOUS));
		}
		list.add(new Link(getHost(request) + request.getRequestURI()
				+  "?page=" + page + "&size=" + size+pathParams, Link.REL_SELF));
		return list;
	}

	public FeedbackResponse getFeedbackResponseByFeedbackScore(FeedbackScore feedbackScore,HttpServletRequest request,HttpServletResponse response){
		FeedbackResponse feedbackResponse = new FeedbackResponse();
		List<String> proofImageUrls = new ArrayList<String>();
		List<String> proofImageIds = new ArrayList<String>();

		
		BeanUtils.copyProperties(feedbackScore, feedbackResponse);
		ShoeCompany shoeCompany = shoeCompanyRepository.findOne(feedbackScore.getCompanyId());
		if(null != shoeCompany){
			feedbackResponse.setCompanyName(shoeCompany.getName());;
		}
		List<FeedbackFile> feedbackFileList = feedbackFileRepository.findByFeedbackId(feedbackScore.getUuid());
		if(null != feedbackFileList && feedbackFileList.isEmpty() == false){
			for(FeedbackFile feedbackFile : feedbackFileList){
				proofImageIds.add(feedbackFile.getFileId());
				proofImageUrls.add(getHost(request)+"/images/show/"+feedbackFile.getFileId());
			}
			feedbackResponse.setProofImageIds(proofImageIds);
		    feedbackResponse.setProofImageUrls(proofImageUrls);
		}
		feedbackResponse.setScoreTypeDesc(ScoreType.getScoreTypeByValue(feedbackScore.getScoreType()).getDesc());
	    Link selfLink = linkTo(methodOn(FeedbackController.class).findOneFeedbackById(feedbackScore.getUuid(), request, response)).withSelfRel();	    
	    feedbackResponse.add(selfLink);
        return feedbackResponse;
    
	}
	
	public String getHost(HttpServletRequest request) {
		int port = request.getServerPort();
		String host = request.getServerName();
		String header = request.getHeader("X-Forwarded-Host");
		if (!StringUtils.isEmpty(header)) {
			return "http://" + header;
		}
		return "http://" + host + ":" + port;
	}

	public Page<FeedbackScore> findFeedbackBySearchKeywordAndAudit(String keyword,Integer status,Pageable pageable) {
		Page<FeedbackScore> feedbackScorePage = feedbackScoreRepository.findFeedbackBySearchKeywordAndAudit(keyword,status,pageable);
		return feedbackScorePage;
	}

	public ResponseEntity<Object> saveFeedbackAndUpdateCompany(
			FeedbackAddRequest feedBackAddRequest, HttpServletRequest request,
			HttpServletResponse response) {
		ShoeCompany shoeCompany = shoeCompanyRepository.findOne(feedBackAddRequest.getCompanyId());
		Integer score = 0;
		if(null != shoeCompany){
			if(feedBackAddRequest.getScoreItem().equalsIgnoreCase(ScoreItems.SERVE_SCORE.getValue())){
				if(feedBackAddRequest.getScoreType().equalsIgnoreCase(ScoreType.PLUS.getValue())){
					score = shoeCompany.getServeScore() + feedBackAddRequest.getScore();
				}else{
					score = shoeCompany.getServeScore() - feedBackAddRequest.getScore();						
				}
				shoeCompany.setServeScore(score);
			}else if(feedBackAddRequest.getScoreItem().equalsIgnoreCase(ScoreItems.CREDIT_SCORE.getValue())){
				if(feedBackAddRequest.getScoreType().equalsIgnoreCase(ScoreType.PLUS.getValue())){
					score = shoeCompany.getCreditScore() + feedBackAddRequest.getScore();
				}else{
					score = shoeCompany.getCreditScore() - feedBackAddRequest.getScore();						
				}
				shoeCompany.setCreditScore(score);
			}else if(feedBackAddRequest.getScoreItem().equalsIgnoreCase(ScoreItems.QUALITY_SCORE.getValue())){
				if(feedBackAddRequest.getScoreType().equalsIgnoreCase(ScoreType.PLUS.getValue())){
					score = shoeCompany.getQualityScore() + feedBackAddRequest.getScore();
				}else{
					score = shoeCompany.getQualityScore() - feedBackAddRequest.getScore();						
				}
				shoeCompany.setQualityScore(score);
			}
			Integer creditLevel = this.getCreditLevel(shoeCompany);
			shoeCompany.setCreditLevel(creditLevel);
			shoeCompanyRepository.save(shoeCompany);
		}
		
		FeedbackScore feedbackScore = this.saveFeedbackScore(feedBackAddRequest, request, response);

		HttpHeaders headers = new HttpHeaders();

		URI selfUrl = linkTo(methodOn(FeedbackController.class).findOneFeedbackById(feedbackScore.getUuid(), request, response)).toUri();
		headers.setLocation(selfUrl);
		return new ResponseEntity<Object>(headers,HttpStatus.CREATED); 
	}
	
	private Integer getCreditLevel(ShoeCompany shoeCompany) {
		// TODO Auto-generated method stub
		Integer totalScore = shoeCompany.getCreditScore()+shoeCompany.getServeScore()+shoeCompany.getQualityScore();
		if(totalScore >= 95){
			return CreditLevel.BEST.getValue();
		}else if(totalScore >= 84 && totalScore < 95){
			return CreditLevel.GENERAL.getValue();
		}else if(totalScore >= 74 && totalScore < 84){
			return CreditLevel.MEDIUM.getValue();
		}else{
			return CreditLevel.INFERIOR.getValue();
		}
	}

	public ResponseEntity<?> delete(String uuid, HttpServletRequest request,
			HttpServletResponse response) {
		FeedbackScore feedbackScore = feedbackScoreRepository.findOne(uuid);
		if(null == feedbackScore){
			logger.info("feedbackScore is not exist");
	        return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
		feedbackScoreRepository.delete(feedbackScore);
		return new ResponseEntity(HttpStatus.OK); 
	}
}
