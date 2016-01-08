package com.ligq.shoe.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ligq.shoe.config.SendMsgConfig;
import com.ligq.shoe.controller.SendMsgController;
import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.model.SendMsgProperties;
import com.ligq.shoe.utils.SendMsgUtil;

@Service
public class SendMsgService {

	private final static Logger logger = LoggerFactory.getLogger(SendMsgController.class); 
	
	@Autowired
	private  SendMsgProperties sendMsgProperties;
	@Autowired
	private  SendMsgConfig sendMsgConfig;
	
	public String sendCheckMsg(SendMsg sendMsg,HttpServletRequest request){
		RestTemplate restTemplate = new RestTemplate();
		StringBuffer url = new StringBuffer();
		
        //短信接口URL提交地址
		url.append(sendMsgConfig.getSendMsgUrl()); 
		url.append("?ac="+sendMsgProperties.getAc());
		url.append("&uid="+sendMsgProperties.getUid());
        url.append("&pwd="+sendMsgProperties.getPwd());
        url.append("&encode="+sendMsgProperties.getEncode());
        url.append("&mobile="+sendMsg.getMobile());
        url.append("&content=尊敬的用户,您的验证码为:"+SendMsgUtil.createRandomVcode()+",有效期为60秒");
		String result = SendMsgUtil.sendMsg(url.toString(),restTemplate);
		HttpSession session = request.getSession(); 
		session.setAttribute(sendMsg.getMobile(), sendMsg);
		logger.info(result);
		return "200";
	}
}
