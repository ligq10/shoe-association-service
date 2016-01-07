package com.ligq.shoe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.ligq.shoe.constants.SendMsgAc;
import com.ligq.shoe.model.SendMsgProperties;
import com.ligq.shoe.utils.MD5Utils;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class SendMsgConfig {

	@Autowired
	private Environment environment;
	
	@Bean
	public SendMsgProperties getSendMsgSendMsgProperties(){
		SendMsgProperties sendMsgProperties = new SendMsgProperties();
		sendMsgProperties.setAc(SendMsgAc.SEND.getValue());
		sendMsgProperties.setUid(environment.getProperty("enterprise.sn"));
		sendMsgProperties.setPwd(MD5Utils.MD5(environment.getProperty("sendmsg.password")));
		sendMsgProperties.setEncode("utf8");
		return sendMsgProperties;
	}
	
	@Bean
	public String getSendMsgUrl(){
		return environment.getProperty("sendmsg.url");
	}
}
