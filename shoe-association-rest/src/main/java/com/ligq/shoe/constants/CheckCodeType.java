package com.ligq.shoe.constants;

/**
 * 获取验证码类型
 * @author ligq
 *
 */
public enum CheckCodeType {

	REGISTER(1,"注册"),
	FEEDBACK(2,"评论");
	
	private Integer value;
	private String desc;
	
	private CheckCodeType(Integer value, String desc){
		this.value = value;
		this.desc = desc;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static CheckCodeType getCheckCodeTypeByValue(Integer value){
		if(null == value){
			return null;
		}
		
		for(CheckCodeType checkCodeType : CheckCodeType.values()){
			if(value == checkCodeType.getValue()){
				return checkCodeType;
			}
		}
		
	    throw new IllegalArgumentException("value值非法，没有符合的枚举对象"); 
	}
}
