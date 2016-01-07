package com.ligq.shoe.constants;

public enum SendMsgAc {

	SEND("send","发送短信"),
	GR("gr","接收回复短信"),
	GC("gc","获取剩余短信条数"),
	GP("gp","修改接口密码");
	
	private String value;
	private String desc;
	
	private SendMsgAc(String value,String desc){
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}	
	
}
