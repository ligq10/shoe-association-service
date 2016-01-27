package com.ligq.shoe.model;

import java.util.List;

public class UpdateUserRoleRequest {

    private String loginName;

    private List<String> roleCodes;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

	public List<String> getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(List<String> roleCodes) {
		this.roleCodes = roleCodes;
	}

}
