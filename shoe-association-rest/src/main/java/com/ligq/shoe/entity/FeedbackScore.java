package com.ligq.shoe.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "feedback_score")
public class FeedbackScore {

	@Id
	@Column(name="id")
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String uuid;
	
	@Column(name = "company_id")
	private String companyId;
	
	@Column(name = "score_type")	
	private String scoreType;
	
	@Column(name = "score_reason")
	private String scoreReason;
	
	@Column(name = "score")
	private Integer score;
	
	@Column(name = "approve_status")
	private Integer approveStatus;
	
	@Column(name = "submit_person")
	private String submitPerson;
	
	@Column(name = "submit_tel")
	private String submitTel;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name = "remark")
	private String remark;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}	
	
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

	public String getSubmitTel() {
		return submitTel;
	}

	public void setSubmitTel(String submitTel) {
		this.submitTel = submitTel;
	}
	
	public String getSubmitPerson() {
		return submitPerson;
	}

	public void setSubmitPerson(String submitPerson) {
		this.submitPerson = submitPerson;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
		
}
