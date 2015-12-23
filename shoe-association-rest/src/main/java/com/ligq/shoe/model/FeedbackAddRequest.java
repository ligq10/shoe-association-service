package com.ligq.shoe.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FeedbackAddRequest {
	
	private String companyId;
	
	private String scoreType;
	
	private Integer score;
	
	private String scoreReason;
	
	private String submitPerson;
	
	private String submitTel;
	
	private List<String> proofFileIds;

	private String remark;	
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}	
	
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getScoreReason() {
		return scoreReason;
	}

	public void setScoreReason(String scoreReason) {
		this.scoreReason = scoreReason;
	}
	
	public String getSubmitPerson() {
		return submitPerson;
	}

	public void setSubmitPerson(String submitPerson) {
		this.submitPerson = submitPerson;
	}

	public String getSubmitTel() {
		return submitTel;
	}

	public void setSubmitTel(String submitTel) {
		this.submitTel = submitTel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<String> getProofFileIds() {
		return proofFileIds;
	}

	public void setProofFileIds(List<String> proofFileIds) {
		this.proofFileIds = proofFileIds;
	}
	
	
}
