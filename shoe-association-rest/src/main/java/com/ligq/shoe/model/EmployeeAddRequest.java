package com.ligq.shoe.model;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ligq.shoe.utils.DateUtils;

@JsonIgnoreProperties(ignoreUnknown=true)
public class EmployeeAddRequest {

	
	private String name;
	
	private String householdRegisterAddress;
	
	private String homeAddress;
	
	private String tel;
	
	private Date birthday;
	
	private String identityCard;

	private String email;
	
	private String gender;
	
	private Integer age;

	private String loginName;
	
	private String password;
	
	private List<String> roleCodes;

	private String  remarks;

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
	
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = DateUtils.composeDate(birthday);
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(List<String> roleCodes) {
		this.roleCodes = roleCodes;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
