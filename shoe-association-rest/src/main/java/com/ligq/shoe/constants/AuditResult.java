package com.ligq.shoe.constants;

import org.springframework.util.StringUtils;

/**
 * 审核结果类型
 * @author ligq
 *
 */
public enum AuditResult {

	PASS("pass","通过"),
	REFUSE("refuse","不通过");
	
	private String value;
	private String desc;
	
	private AuditResult(String value, String desc){
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	
	public static AuditResult getAuditResultByValue(String value){
		if(StringUtils.isEmpty(value)){
			return null;
		}
		
		for(AuditResult auditResult : AuditResult.values()){
			if(value == auditResult.getValue()){
				return auditResult;
			}
		}
		
	    throw new IllegalArgumentException("value值非法，没有符合的枚举对象"); 
	}
}
