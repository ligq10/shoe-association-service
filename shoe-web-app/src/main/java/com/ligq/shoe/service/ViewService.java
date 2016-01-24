package com.ligq.shoe.service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ligq.shoe.model.EmployeeResponse;
import com.ligq.shoe.utils.BeanToMapUtils;

@Service
public class ViewService {

	private static final Logger logger = LoggerFactory.getLogger(ViewService.class);

	
	@Autowired
	private Environment environment;
	
	public EmployeeResponse getEmployeeByLoginId(String loginId,String accessToken){
		EmployeeResponse user = new EmployeeResponse();
		String employeeUrl = environment.getRequiredProperty("employeeServer.address")+"/"+loginId;
		ResponseEntity responseEntity = null;
		try {
			responseEntity = getEntityByUUID(accessToken,employeeUrl);
			 HttpStatus status = responseEntity.getStatusCode();
			 if(null != status && status.equals(HttpStatus.OK)){
				java.util.Map<String,Object> entityMap = (java.util.Map<String, Object>) responseEntity.getBody();
				user = (EmployeeResponse) BeanToMapUtils.convertMap(EmployeeResponse.class, entityMap);
			 }
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			logger.error("RestClientException:",e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("ClassNotFoundException:",e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			logger.error("IllegalAccessException:",e);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			logger.error("InstantiationException:",e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.error("InvocationTargetException:",e);
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			logger.error("IntrospectionException:",e);
		}

		 return user;
	}
	
	 private <T> T getEntityByUUID(String token,String url) throws RestClientException, ClassNotFoundException{
	 	 	RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Token",token);;
			return (T) restTemplate.exchange(url,
					HttpMethod.GET, new HttpEntity<>(headers), Object.class);
	 }
}
