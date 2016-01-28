package com.ligq.shoe.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ligq.shoe.utils.DateUtils;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FeedbackResponse extends ResourceSupport{
	
	private String uuid;

	private String companyName;
	
	private String companyId;
	
	private String scoreType;
	
	private String scoreTypeDesc;
	
	private String scoreReason;
	
	private String submitPerson;
	
	private String submitTel;

	private String remark;
	
	private Integer score;
	
	private String createTime;
	
	private List<String> proofImageIds;
	
	private List<String> proofImageUrls;

	public String getUuid() {
		if(StringUtils.isEmpty(uuid)){
			uuid = "";
		}
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getScoreType() {
		if(StringUtils.isEmpty(scoreType)){
			scoreType = "";
		}
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public String getScoreReason() {
		if(StringUtils.isEmpty(scoreReason)){
			scoreReason = "";
		}
		return scoreReason;
	}

	public void setScoreReason(String scoreReason) {
		this.scoreReason = scoreReason;
	}
	
	public String getSubmitPerson() {
		if(StringUtils.isEmpty(submitPerson)){
			submitPerson = "";
		}
		return submitPerson;
	}

	public void setSubmitPerson(String submitPerson) {
		this.submitPerson = submitPerson;
	}

	public String getCreateTime() {
		if(StringUtils.isEmpty(createTime)){
			createTime = "";
		}
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = DateUtils.composeUTCTime(createTime);
	}

	public String getSubmitTel() {
		if(StringUtils.isEmpty(submitTel)){
			submitTel = "";
		}
		return submitTel;
	}

	public void setSubmitTel(String submitTel) {
		this.submitTel = submitTel;
	}

	public String getRemark() {
		if(StringUtils.isEmpty(remark)){
			remark = "";
		}
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getScore() {
		if(StringUtils.isEmpty(score)){
			score = 0;
		}
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public List<String> getProofImageUrls() {
		if(null == proofImageUrls){
			proofImageUrls = new ArrayList<String>();
		}
		return proofImageUrls;
	}

	public void setProofImageUrls(List<String> proofImageUrls) {
		this.proofImageUrls = proofImageUrls;
	}

	public List<String> getProofImageIds() {
		return proofImageIds;
	}

	public void setProofImageIds(List<String> proofImageIds) {
		this.proofImageIds = proofImageIds;
	}

	public String getCompanyName() {
		if(StringUtils.isEmpty(companyName)){
			companyName = "";
		}
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyId() {
		if(StringUtils.isEmpty(companyId)){
			companyId = "";
		}
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getScoreTypeDesc() {
		if(StringUtils.isEmpty(scoreTypeDesc)){
			scoreTypeDesc = "";
		}
		return scoreTypeDesc;
	}

	public void setScoreTypeDesc(String scoreTypeDesc) {
		this.scoreTypeDesc = scoreTypeDesc;
	}
	
	
}
