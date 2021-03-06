package com.ligq.shoe.model;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ligq.shoe.service.DataDictService;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SendMsg extends SendMsgProperties{

	private final static Logger logger = LoggerFactory.getLogger(DataDictService.class); 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mobile;
	
	private String content;
	
	private Integer type;
	
	private String checkCode;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;	
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
}
