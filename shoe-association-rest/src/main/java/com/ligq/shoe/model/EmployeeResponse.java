package com.ligq.shoe.model;

import java.util.Date;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ligq.shoe.utils.DateUtils;

@JsonIgnoreProperties(ignoreUnknown=true)
public class EmployeeResponse extends ResourceSupport{

	private String uuid;
	
	private String name;
	
	private String householdRegisterAddress;
	
	private String homeAddress;
	
	private String tel;
	
	private String identityCard;

	private String email;
	
	private String gender;
	
	private Integer age;
	
	private String birthday;

	private String loginName;
		
	private String createTime;
	
	private List<RoleResponse> roles;
	
	public String getUuid() {
		if(StringUtils.isEmpty(uuid)){
			uuid = "";
		}
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		if(StringUtils.isEmpty(name)){
			name = "";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHouseholdRegisterAddress() {
		if(StringUtils.isEmpty(householdRegisterAddress)){
			householdRegisterAddress = "";
		}
		return householdRegisterAddress;
	}

	public void setHouseholdRegisterAddress(String householdRegisterAddress) {
		this.householdRegisterAddress = householdRegisterAddress;
	}

	public String getHomeAddress() {
		if(StringUtils.isEmpty(homeAddress)){
			homeAddress = "";
		}		
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
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

	public String getIdentityCard() {
		if(StringUtils.isEmpty(identityCard)){
			identityCard = "";
		}
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getEmail() {
		if(StringUtils.isEmpty(email)){
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		if(StringUtils.isEmpty(gender)){
			gender = "";
		}
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		if(null == age){
			age = 0;
		}
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getLoginName() {
		if(StringUtils.isEmpty(loginName)){
			loginName = "";
		}
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
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

	public List<RoleResponse> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleResponse> roles) {
		this.roles = roles;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = DateUtils.composeUTCDate(birthday);
	}

}
