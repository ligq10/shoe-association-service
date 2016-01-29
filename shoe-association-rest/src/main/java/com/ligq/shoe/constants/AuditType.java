package com.ligq.shoe.constants;

import org.springframework.util.StringUtils;

/**
 * 审核类型
 * @author ligq
 *
 */
public enum AuditType {

	REGISTER("register","注册"),
	FEEDBACK("feedback","评论");
	
	private String value;
	private String desc;
	
	private AuditType(String value, String desc){
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	
	public static AuditType getAuditTypeByValue(String value){
		if(StringUtils.isEmpty(value)){
			return null;
		}
		
		for(AuditType auditType : AuditType.values()){
			if(value == auditType.getValue()){
				return auditType;
			}
		}
		
	    throw new IllegalArgumentException("value值非法，没有符合的枚举对象"); 
	}
}
