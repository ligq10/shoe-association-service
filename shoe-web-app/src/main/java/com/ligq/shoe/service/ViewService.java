package com.ligq.shoe.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ligq.shoe.model.EmployeeResponse;

@Service
public class ViewService {

	private static final Logger logger = LoggerFactory.getLogger(ViewService.class);

	
	@Autowired
	private Environment environment;
	
	public EmployeeResponse getEmployeeByLoginId(String loginId,String accessToken){
		EmployeeResponse user = new EmployeeResponse();
		String employeeUrl = environment.getRequiredProperty("employeeServer.address")+"/"+loginId;
		try {
			user = getEntityByUUID(accessToken,employeeUrl);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			logger.error("RestClientException:",e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("ClassNotFoundException:",e);
		}

		 return user;
	}
	
	 private EmployeeResponse getEntityByUUID(String token,String url) throws RestClientException, ClassNotFoundException{
	 	 	RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Token",token);;
			return  restTemplate.getForObject(String.format(url, token),EmployeeResponse.class);
	 }
}
