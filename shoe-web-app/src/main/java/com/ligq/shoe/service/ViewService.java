package com.ligq.shoe.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ligq.shoe.model.EmployeeResponse;
import com.ligq.shoe.model.RoleResponse;

@Service
public class ViewService {

	private static final Logger logger = LoggerFactory.getLogger(ViewService.class);
    private static final String SECURITY_TOKEN_HEADER = "X-Token";

	
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
			headers.add(SECURITY_TOKEN_HEADER,token);;
			return  restTemplate.getForObject(String.format(url, token),EmployeeResponse.class);
	 }
	 
	public List<RoleResponse> findRoles(String token) {
		List<RoleResponse> roles = new ArrayList<RoleResponse>();
 	 	RestTemplate restTemplate = new RestTemplate();
		
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.put("Content-Type", Lists.newArrayList(MediaType.APPLICATION_JSON_VALUE));
		headers.put(SECURITY_TOKEN_HEADER, Lists.newArrayList(token));
		
		String rolesAddress = environment.getRequiredProperty("oauth2Roles.endpoint");
		try {
			ResponseEntity responseEntity = restTemplate.exchange(rolesAddress, HttpMethod.GET, new HttpEntity<MultiValueMap>(headers), Object.class);

			if(null != responseEntity || responseEntity.getStatusCode().equals(HttpStatus.OK)){
				Map rolesMap = (Map)responseEntity.getBody();
				JSONObject jsonObjct = JSONObject.fromObject(responseEntity.getBody());
				JSONObject page = (JSONObject)jsonObjct.get("page");
				jsonObjct=(JSONObject)jsonObjct.get("_embedded");
				
				JSONArray jsonarray= JSONArray.fromObject(jsonObjct.get("roleResponses"));
				roles=JSON.parseArray(jsonarray.toString(), RoleResponse.class );
			}

		} catch (RestClientException | IllegalStateException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			return null;
		}

		return roles;
	}
}
