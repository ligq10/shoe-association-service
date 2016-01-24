package com.ligq.shoe.model;

import java.util.Date;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ligq.shoe.utils.DateUtils;

@JsonIgnoreProperties(ignoreUnknown=true)
public class EmployeeResponse{

	private String uuid;
	
	private String name;
	
	private String householdRegisterAddress;
	
	private String homeAddress;
	
	private String tel;
	
	private String identityCard;

	private String email;
	
	private String gender;
	
	private Integer age;

	private String loginName;
		
	private String createTime;
	
	private String accessToken;
	
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

	public String getHouseholdRegisterAddress() {
		return householdRegisterAddress;
	}

	public void setHouseholdRegisterAddress(String householdRegisterAddress) {
		this.householdRegisterAddress = householdRegisterAddress;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = DateUtils.composeUTCTime(createTime);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
