package com.ligq.shoe.constants;

public enum FeedbackAuditStatus {

	WAITING_AUDIT(0,"待审核"),
	PASS_AUDIT(1,"初级审核通过"),
	REFUSE_AUDIT(2,"初级审核不通过"),
	PRIMARY_PASS_AUDIT(3,"终级审核通过"),
	MIDDLE_REFUSE_AUDIT(4,"终级审核不通过");
	
	private Integer value;
	private String desc;
	
	private FeedbackAuditStatus(Integer value,String desc){
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
	
	public static FeedbackAuditStatus getFeedbackAuditStatus(Integer value){
		if(null == value){
			return null;
		}
		for(FeedbackAuditStatus feedbackAuditStatus : FeedbackAuditStatus.values()){
			if(value == feedbackAuditStatus.getValue()){
				return feedbackAuditStatus;
			}
		}
	    throw new IllegalArgumentException("value值非法，没有符合的枚举对象"); 
	}
}
