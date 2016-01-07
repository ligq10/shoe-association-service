package com.ligq.shoe.constants;

public enum SendMsgResponseFlag {

	SEND_SUCCESS(100,"发送成功"),
	VAILD_FAIL(101,"验证失败"),
	LACK_MESSAGE(102,"短信不足"),
	OPERATE_ERROR(103,"操作失败"),
	INVALID_CHAR(104,"无效字符"),
	CONTENT_LONG(105,"内容过多"),
	MOBILE_LONG(106,"号码过多"),
	FREQUENCY_QUIK(107,"频率过快"),
	MOBILE_EMPTY(108,"号码内容为空"),
	ACCOUNT_FREEZE(109,"账号冻结"),
	STOP_ONE_FREQUENCY_SEND(110,"禁止单条频繁发送"),
	SYSTEM_PAUSE_SEND(111,"系统暂定发送"),
	PHONE_NUMBER_ERROR(112,"号码错误"),
	TIMER_TIME_ERROR(113,"定时时间错误"),
	ACCOUNT_LOCK(114,"账号被锁定，10分钟后再登录"),
	COONECT_ERROR(115,"连接失败");
	
	private Integer value;
	private String desc;
	
	private SendMsgResponseFlag(Integer value,String desc){
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
	
	public SendMsgResponseFlag getSendMsgResponseFlagByValue(Integer value){
		if(null == value){
			return null;
		}
		for(SendMsgResponseFlag sendMsgResponseFlag : SendMsgResponseFlag.values()){
			if(value == sendMsgResponseFlag.getValue()){
				return sendMsgResponseFlag;
			}
		}
		
	    throw new IllegalArgumentException("value值非法，没有符合的枚举对象"); 
	}
}
