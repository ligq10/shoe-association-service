package com.ligq.shoe.model;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ligq.shoe.service.DataDictService;

public class SendMsg extends SendMsgProperties{

	private final static Logger logger = LoggerFactory.getLogger(DataDictService.class); 

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mobile;
	
	private String content;

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
		try {
			this.content = java.net.URLEncoder.encode(content,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.info(e.getMessage(),e);
			this.content = content;
		}
	}
	
	
}
