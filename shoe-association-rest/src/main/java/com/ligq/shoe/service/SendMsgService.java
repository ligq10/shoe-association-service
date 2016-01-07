package com.ligq.shoe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ligq.shoe.config.SendMsgConfig;
import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.model.SendMsgProperties;
import com.ligq.shoe.utils.SendMsgUtil;

@Service
public class SendMsgService {

	@Autowired
	private  SendMsgProperties sendMsgProperties;
	@Autowired
	private  SendMsgConfig sendMsgConfig;
	
	public String sendCheckMsg(SendMsg sendMsg){
		RestTemplate restTemplate = new RestTemplate();
		StringBuffer url = new StringBuffer();
		
        //短信接口URL提交地址
		url.append(sendMsgConfig.getSendMsgUrl()); 
		url.append("?ac="+sendMsgProperties.getAc());
		url.append("&uid="+sendMsgProperties.getUid());
        url.append("&pwd="+sendMsgProperties.getPwd());
        url.append("&encode="+sendMsgProperties.getEncode());
        url.append("&mobile="+sendMsg.getMobile());
        url.append("&content="+sendMsg.getContent());
        //Map<String, String> params = new HashMap<String, String>();
		SendMsgUtil.sendMsg(url.toString(),restTemplate);

		return "200";
	}
}
