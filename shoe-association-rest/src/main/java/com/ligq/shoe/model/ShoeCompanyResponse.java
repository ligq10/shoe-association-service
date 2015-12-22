package com.ligq.shoe.model;


import java.util.Date;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ligq.shoe.utils.DateUtils;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ShoeCompanyResponse extends ResourceSupport{

	private String name;
	
	private String address;
	
	private String enterpriseLegalPerson;
	
	private String submitPerson;
	
	private String tel;
	
	private String createTime;

	private String updateTime;
	
	private Integer comprehensiveScore;

	private String logoImageUrl;
	
	private String logoImageId;

	private String permitImageId;

	private String permitImageUrl;
	
	private Integer totalScore;
	
	private Integer creditScore;

	private Integer qualityScore;

	private Integer serveScore;

	private Integer creditLevel;

	private String creditDesc;
	
	public String getName() {
		if(StringUtils.isEmpty(name)){
			name = "";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		if(StringUtils.isEmpty(address)){
			address = "";
		}
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEnterpriseLegalPerson() {
		if(StringUtils.isEmpty(enterpriseLegalPerson)){
			enterpriseLegalPerson = "";
		}
		return enterpriseLegalPerson;
	}

	public void setEnterpriseLegalPerson(String enterpriseLegalPerson) {
		this.enterpriseLegalPerson = enterpriseLegalPerson;
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

	public String getTel() {
		if(StringUtils.isEmpty(tel)){
			tel = "";
		}
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getComprehensiveScore() {
		if(StringUtils.isEmpty(comprehensiveScore)){
			comprehensiveScore = 0;
		}
		return comprehensiveScore;
	}

	public void setComprehensiveScore(Integer comprehensiveScore) {
		this.comprehensiveScore = comprehensiveScore;
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

	public String getUpdateTime() {
		if(StringUtils.isEmpty(updateTime)){
			updateTime = "";
		}
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = DateUtils.composeUTCTime(updateTime);
	}

	public Integer getTotalScore() {
		if(StringUtils.isEmpty(totalScore)){
			totalScore = 0;
		}
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getCreditScore() {
		if(StringUtils.isEmpty(creditScore)){
			creditScore = 0;
		}
		return creditScore;
	}

	public void setCreditScore(Integer creditScore) {
		this.creditScore = creditScore;
	}

	public Integer getQualityScore() {
		if(StringUtils.isEmpty(qualityScore)){
			qualityScore = 0;
		}
		return qualityScore;
	}

	public void setQualityScore(Integer qualityScore) {
		this.qualityScore = qualityScore;
	}

	public Integer getServeScore() {
		if(StringUtils.isEmpty(serveScore)){
			serveScore = 0;
		}
		return serveScore;
	}

	public void setServeScore(Integer serveScore) {
		this.serveScore = serveScore;
	}

	public Integer getCreditLevel() {
		if(StringUtils.isEmpty(creditLevel)){
			creditLevel = 0;
		}
		return creditLevel;
	}

	public void setCreditLevel(Integer creditLevel) {
		this.creditLevel = creditLevel;
	}

	public String getLogoImageUrl() {
		if(StringUtils.isEmpty(logoImageUrl)){
			logoImageUrl = "";
		}
		return logoImageUrl;
	}

	public void setLogoImageUrl(String logoImageUrl) {
		this.logoImageUrl = logoImageUrl;
	}

	public String getLogoImageId() {
		if(StringUtils.isEmpty(logoImageId)){
			logoImageId = "";
		}
		return logoImageId;
	}

	public void setLogoImageId(String logoImageId) {
		this.logoImageId = logoImageId;
	}

	public String getPermitImageId() {
		if(StringUtils.isEmpty(permitImageId)){
			permitImageId = "";
		}
		return permitImageId;
	}

	public void setPermitImageId(String permitImageId) {
		this.permitImageId = permitImageId;
	}

	public String getPermitImageUrl() {
		if(StringUtils.isEmpty(permitImageUrl)){
			permitImageUrl = "";
		}
		return permitImageUrl;
	}

	public void setPermitImageUrl(String permitImageUrl) {
		this.permitImageUrl = permitImageUrl;
	}

	public String getCreditDesc() {
		if(StringUtils.isEmpty(creditDesc)){
			creditDesc = "";
		}
		return creditDesc;
	}

	public void setCreditDesc(String creditDesc) {
		this.creditDesc = creditDesc;
	}
	
}
