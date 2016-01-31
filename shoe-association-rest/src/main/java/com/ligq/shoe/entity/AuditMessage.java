package com.ligq.shoe.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "audit_message")
public class AuditMessage {

	@Id
	@Column(name="uuid")
	private String uuid;
	
	@Column(name="business_id")
	private String businessId;
	
	@Column(name="business_type")
	private String businessType;
		
	@Column(name="auditor")
	private String auditor;
	
	@Column(name="auditor_id")
	private String auditorId;
	
	@Column(name="audit_result")
	private String auditResult;

	@Column(name="audit_remark")
	private String auditRemark;
	
	@Column(name="score_type")
	private String scoreType;
	
	@Column(name="score_item")
	private String scoreItem;
	
	@Column(name="score")
	private Integer score;

	@Column(name="audit_status_value")
	private Integer auditStatusValue;
	
	@Column(name="audit_status_desc")
	private String auditStatusDesc;
	
	@Column(name="create_time")
	private Date createTime;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}

	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	
	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public String getScoreItem() {
		return scoreItem;
	}

	public void setScoreItem(String scoreItem) {
		this.scoreItem = scoreItem;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getAuditStatusValue() {
		return auditStatusValue;
	}

	public void setAuditStatusValue(Integer auditStatusValue) {
		this.auditStatusValue = auditStatusValue;
	}

	public String getAuditStatusDesc() {
		return auditStatusDesc;
	}

	public void setAuditStatusDesc(String auditStatusDesc) {
		this.auditStatusDesc = auditStatusDesc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
		
}
