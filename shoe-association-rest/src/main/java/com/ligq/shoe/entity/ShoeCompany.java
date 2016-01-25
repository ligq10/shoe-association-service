package com.ligq.shoe.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;


@Entity
@Table(name = "shoe_company")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoeCompany {
	
	@Id
	@Column(name="id")
	private String uuid;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "update_time")
	private Date updateTime;
	
	@Column(name = "credit_score")
	private Integer creditScore;
	
	@Column(name = "quality_score")
	private Integer qualityScore;
	
	@Column(name = "serve_score")
	private Integer serveScore;
	
	@Column(name = "logo_image_id")
	private String logoImageId;
	
	@Column(name = "permit_image_id")
	private String permitImageId;
	
	@Column(name = "enterprise_legal_person")
	private String enterpriseLegalPerson;
	
	@Column(name = "submit_person")
	private String submitPerson;
	
	@Column(name = "tel")
	private String tel;
	
	@Column(name = "name_phoneticize")
	private String namePhoneticize;
	
	@Column(name = "credit_level")
	private Integer creditLevel;

	@Column(name = "audit_status")
	private Integer auditStatus;
	

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(Integer creditScore) {
		this.creditScore = creditScore;
	}

	public Integer getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(Integer qualityScore) {
		this.qualityScore = qualityScore;
	}

	public Integer getServeScore() {
		return serveScore;
	}

	public void setServeScore(Integer serveScore) {
		this.serveScore = serveScore;
	}

	public String getLogoImageId() {
		return logoImageId;
	}

	public void setLogoImageId(String logoImageId) {
		this.logoImageId = logoImageId;
	}

	public String getPermitImageId() {
		return permitImageId;
	}

	public void setPermitImageId(String permitImageId) {
		this.permitImageId = permitImageId;
	}

	public String getEnterpriseLegalPerson() {
		return enterpriseLegalPerson;
	}

	public void setEnterpriseLegalPerson(String enterpriseLegalPerson) {
		this.enterpriseLegalPerson = enterpriseLegalPerson;
	}

	public String getNamePhoneticize() {
		return namePhoneticize;
	}

	public void setNamePhoneticize(String namePhoneticize) {
		this.namePhoneticize = namePhoneticize;
	}

	public Integer getCreditLevel() {
		return creditLevel;
	}

	public void setCreditLevel(Integer creditLevel) {
		this.creditLevel = creditLevel;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getSubmitPerson() {
		return submitPerson;
	}

	public void setSubmitPerson(String submitPerson) {
		this.submitPerson = submitPerson;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
