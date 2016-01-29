package com.ligq.shoe.model;

import java.util.Date;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ligq.shoe.utils.DateUtils;

@JsonIgnoreProperties
public class AuditMessageResponse extends ResourceSupport{
	
	private String uuid;

	private String businessId;
	
	private String businessType;
	
	private String auditor;
	
	private String auditorId;
		
	private String auditResult;

	private String auditRemark;
	
	private String createTime;

	public String getBusinessId() {
		if(StringUtils.isEmpty(businessId)){
			businessId = "";
		}
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessType() {
		if(StringUtils.isEmpty(businessType)){
			businessType = "";
		}
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getAuditor() {
		if(StringUtils.isEmpty(auditor)){
			auditor = "";
		}
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getAuditorId() {
		if(StringUtils.isEmpty(auditorId)){
			auditorId = "";
		}		
		return auditorId;
	}

	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditResult() {
		if(StringUtils.isEmpty(auditResult)){
			auditResult = "";
		}
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}

	public String getAuditRemark() {
		if(StringUtils.isEmpty(auditRemark)){
			auditRemark = "";
		}		
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	public String getUuid() {
		if(StringUtils.isEmpty(uuid)){
			uuid = "";
		}
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
		
}
