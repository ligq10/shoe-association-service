package com.ligq.shoe.model;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserResponse extends ResourceSupport{

    private String uuid;
    
    private String loginName;

    private List<RoleResponse> Roles;
    
    public UserResponse() {

    }  
    
    public UserResponse(String uuid, String loginName) {
        this.uuid = uuid;
        this.loginName = loginName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }   

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public List<RoleResponse> getRoles() {
		return Roles;
	}

	public void setRoles(List<RoleResponse> roles) {
		Roles = roles;
	}
   
}
