package com.ligq.shoe.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FeedbackAddRequest {
	
	private String companyId;
	
	private String scoreType;
	
	private String scoreReason;
	
	private String submit_person;
	
	private String submitTel;

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

	public String getScoreReason() {
		return scoreReason;
	}

	public void setScoreReason(String scoreReason) {
		this.scoreReason = scoreReason;
	}

	public String getSubmit_person() {
		return submit_person;
	}

	public void setSubmit_person(String submit_person) {
		this.submit_person = submit_person;
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
	
	
}
