package com.ligq.shoe.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ligq.shoe.config.SendMsgConfig;
import com.ligq.shoe.controller.SendMsgController;
import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.model.SendMsgProperties;
import com.ligq.shoe.utils.BeanUtils;
import com.ligq.shoe.utils.SendMsgUtil;

@Service
public class SendMsgService {

	private final static Logger logger = LoggerFactory.getLogger(SendMsgController.class); 
	
	@Autowired
	private  SendMsgProperties sendMsgProperties;
	@Autowired
	private  SendMsgConfig sendMsgConfig;
	
	public ResponseEntity<?> sendCheckMsg(SendMsg sendMsg,HttpServletRequest request){
		RestTemplate restTemplate = new RestTemplate();
		StringBuffer url = new StringBuffer();
		//BeanUtils.copyPropertiesIgnoreNull(sendMsgProperties, sendMsg);
		//String checkCode = SendMsgUtil.createRandomVcode();
		//sendMsg.setContent(checkCode);
		String content = "";
		try {
			content = URLEncoder.encode(sendMsg.getContent(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			return new ResponseEntity<String>("发送内容有误",HttpStatus.INTERNAL_SERVER_ERROR);
		}
        //短信接口URL提交地址
		url.append(sendMsgConfig.getSendMsgUrl()); 
		url.append("?ac="+sendMsgProperties.getAc());
		url.append("&uid="+sendMsgProperties.getUid());
        url.append("&pwd="+sendMsgProperties.getPwd());
        url.append("&encode="+sendMsgProperties.getEncode());
        url.append("&mobile="+sendMsg.getMobile());
        url.append("&content="+content);
        ResponseEntity<?> result = SendMsgUtil.sendMsg(url.toString(),restTemplate);
		return result;
	}
}
