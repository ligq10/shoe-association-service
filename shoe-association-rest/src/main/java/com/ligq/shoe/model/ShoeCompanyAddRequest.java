package com.ligq.shoe.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ShoeCompanyAddRequest {

	private String name;
	
	private String address;
	
	private String enterpriseLegalPerson;
	
	private String submitPerson;
	
	private String tel;
	
	private String logoImageId;
	
	private String permitImageId;
	
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

	public String getEnterpriseLegalPerson() {
		return enterpriseLegalPerson;
	}

	public void setEnterpriseLegalPerson(String enterpriseLegalPerson) {
		this.enterpriseLegalPerson = enterpriseLegalPerson;
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
	
}
