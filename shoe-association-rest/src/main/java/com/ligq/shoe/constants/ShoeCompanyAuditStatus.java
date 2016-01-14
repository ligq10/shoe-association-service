package com.ligq.shoe.constants;

public enum ShoeCompanyAuditStatus {

	WAITING_AUDIT(0,"待审核"),
	PASS_AUDIT(1,"审核通过"),
	REFUSE_AUDIT(2,"审核不通过");
	
	private Integer value;
	private String desc;
	
	private ShoeCompanyAuditStatus(Integer value,String desc){
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
	
	public static ShoeCompanyAuditStatus getShoeCompanyAuditStatus(Integer value){
		if(null == value){
			return null;
		}
		for(ShoeCompanyAuditStatus shoeCompanyAuditStatus : ShoeCompanyAuditStatus.values()){
			if(value == shoeCompanyAuditStatus.getValue()){
				return shoeCompanyAuditStatus;
			}
		}
	    throw new IllegalArgumentException("value值非法，没有符合的枚举对象"); 
	}
}
