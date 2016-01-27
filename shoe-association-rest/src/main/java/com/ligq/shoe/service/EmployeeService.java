package com.ligq.shoe.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ligq.shoe.controller.EmployeeContorller;
import com.ligq.shoe.entity.Employee;
import com.ligq.shoe.model.EmployeeAddRequest;
import com.ligq.shoe.model.EmployeeResponse;
import com.ligq.shoe.model.RegisterUser;
import com.ligq.shoe.model.RoleResponse;
import com.ligq.shoe.model.UpdatePasswordRequest;
import com.ligq.shoe.model.UpdateUserRoleRequest;
import com.ligq.shoe.model.UserResponse;
import com.ligq.shoe.repository.EmployeeRepository;
import com.ligq.shoe.utils.BeanUtils;

@Service
public class EmployeeService {

	private final static Logger logger = LoggerFactory.getLogger(EmployeeService.class); 
	private final String SECURITY_TOKEN_HEADER = "X-Token";	

	@Autowired
	private Environment env;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public  Employee findOneUserById(String uuid){
		Employee employeeEntity = employeeRepository.findOne(uuid);
		return employeeEntity;
	}

	public ResponseEntity<Object> save(EmployeeAddRequest employeeAddRequest,
			HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(SECURITY_TOKEN_HEADER);
		RegisterUser registerUser = new RegisterUser();
		registerUser.setLoginName(employeeAddRequest.getLoginName());
		registerUser.setPassword(employeeAddRequest.getPassword());
		registerUser.setRoleCodes(employeeAddRequest.getRoleCodes());
		boolean isSuccessFlag = false;
		try {
			isSuccessFlag = this.registerUserToOauth2Service(registerUser,token);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return new ResponseEntity<Object>("同步账户失败",HttpStatus.BAD_GATEWAY);
		}
		if(isSuccessFlag == false){
			return new ResponseEntity<Object>("同步账户失败",HttpStatus.BAD_REQUEST);
		}
		Employee employeeEntity = new Employee();
		BeanUtils.copyProperties(employeeAddRequest, employeeEntity);
		Date createTime = new Date();
		String uuid = getUuidByloginName(employeeAddRequest.getLoginName());
		employeeEntity.setUuid(uuid);
		employeeEntity.setCreateTime(createTime);
		employeeEntity.setUpdateTime(createTime);
		employeeRepository.save(employeeEntity);
		
		HttpHeaders headers = new HttpHeaders();
		URI selfUrl = linkTo(methodOn(EmployeeContorller.class).findOneEmployeeById(employeeEntity.getUuid(), request, response)).toUri();
		headers.setLocation(selfUrl);
		return new ResponseEntity<Object>(headers,HttpStatus.CREATED);

	}

	public Page<Employee> findAllEmployeeByKeword(String keyword,
			Pageable pageRequest) {
		Page<Employee> employeePage = employeeRepository.findByKeyword(keyword,pageRequest);
		return employeePage;
	}
	
	public ResponseEntity getResponseEntityConvertEmployeePage(String pathParams,Page<Employee> employeeResult,
			Pageable pageable,HttpServletRequest request,HttpServletResponse response)throws Exception{
		String token = request.getHeader(SECURITY_TOKEN_HEADER);

		List<Link> list = prepareLinks(pageable.getPageNumber(),
				pageable.getPageSize(), request, employeeResult,pathParams);
		List<EmployeeResponse> content = new ArrayList<EmployeeResponse>();
		
		if(null != employeeResult){
			for (Employee employee : employeeResult.getContent()) {

				EmployeeResponse employeeResponse = new EmployeeResponse();
				BeanUtils.copyProperties(employee, employeeResponse);
			    Link selfLink = linkTo(methodOn(EmployeeContorller.class).findOneEmployeeById(employee.getUuid(), request, response)).withSelfRel();	    
			    employeeResponse.add(selfLink);
				List<RoleResponse> roles = this.findRolesByUserUuid(employee.getUuid(),token);
				if(null != roles){
					employeeResponse.setRoles(roles);
				}
				content.add(employeeResponse);
			}			
		}
		
		PagedResources<EmployeeResponse> pagedResources = new PagedResources<EmployeeResponse>(
				content, new PageMetadata(employeeResult.getSize(), employeeResult.getNumber(),
						employeeResult.getTotalElements(), employeeResult.getTotalPages()),
				list);
		return new ResponseEntity(pagedResources, HttpStatus.OK); 
	}

	private List<Link> prepareLinks(int page, int size,
			HttpServletRequest request, Page result,String pathParams) {
		List<Link> list = new ArrayList<>();
		if (result.hasNext()) {
			list.add(new Link(getHost(request) + request.getRequestURI()
					+  "?page=" + (page + 1) + "&size=" + size+pathParams,
					Link.REL_NEXT));
		}
		if (result.hasPrevious()) {
			list.add(new Link(getHost(request) + request.getRequestURI()
					+ "?page=" + (page - 1) + "&size=" + size+pathParams,
					Link.REL_PREVIOUS));
		}
		list.add(new Link(getHost(request) + request.getRequestURI()
				+  "?page=" + page + "&size=" + size+pathParams, Link.REL_SELF));
		return list;
	}
	
	public String getHost(HttpServletRequest request) {
		int port = request.getServerPort();
		String host = request.getServerName();
		String header = request.getHeader("X-Forwarded-Host");
		if (!StringUtils.isEmpty(header)) {
			return "http://" + header;
		}
		return "http://" + host + ":" + port;
	}

	public Employee findByLoginName(String loginName) {
		// TODO Auto-generated method stub
		Employee employee = employeeRepository.findByLoginName(loginName);
		return employee;
	}

	private String getUuidByloginName(String loginName){
		return UUID.nameUUIDFromBytes(loginName.getBytes()).toString();
	}

	public boolean registerUserToOauth2Service(RegisterUser registerUser,String token) throws Exception {
		RestTemplateResponseErrorHandler responseErrorHandler = new RestTemplateResponseErrorHandler();
		boolean isSuccessFlag = false;
		try{
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.put("Content-Type",
					Lists.newArrayList(MediaType.APPLICATION_JSON_VALUE));
			headers.put(SECURITY_TOKEN_HEADER, Lists.newArrayList(token));
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setErrorHandler(responseErrorHandler);
			ResponseEntity responseBody =restTemplate.exchange(env.getRequiredProperty("userRegister.endpoint"),
					HttpMethod.POST, new HttpEntity<>(registerUser,
							headers), ResponseEntity.class);
			if(responseBody.getStatusCode().equals(HttpStatus.CREATED)){
				isSuccessFlag = true; 
			}
		}catch(Exception e){
			logger.error("regist to remote server failed!",e);
			return false;
		}
		return isSuccessFlag;
	}

	public List<RoleResponse> findRolesByUserUuid(
			String uuid,String token) {
		List<RoleResponse> roles = new ArrayList<RoleResponse>();
 	 	RestTemplate restTemplate = new RestTemplate();
		
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.put("Content-Type", Lists.newArrayList(MediaType.APPLICATION_JSON_VALUE));
		headers.put(SECURITY_TOKEN_HEADER, Lists.newArrayList(token));
		
		String rolesAddress = env.getRequiredProperty("oauth2User.endpoint")+"/"+uuid;
		try {
			ResponseEntity responseEntity = restTemplate.exchange(rolesAddress, HttpMethod.GET, new HttpEntity<MultiValueMap>(headers), Object.class);

			if(null != responseEntity || responseEntity.getStatusCode().equals(HttpStatus.OK)){
				Map rolesMap = (Map)responseEntity.getBody();
				JSONObject jsonObjct = JSONObject.fromObject(responseEntity.getBody());
				
				JSONArray jsonarray= JSONArray.fromObject(jsonObjct.get("roles"));
				roles=JSON.parseArray(jsonarray.toString(), RoleResponse.class );
			}

		} catch (RestClientException | IllegalStateException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			return null;
		}

		return roles;
	}

	public ResponseEntity<?> update(String uuid,
			EmployeeAddRequest employeeAddRequest, HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getHeader(SECURITY_TOKEN_HEADER);
		Employee employeeEntity = this.findOneUserById(uuid);
		if(null == employeeEntity){
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		
		if(StringUtils.isEmpty(employeeAddRequest.getPassword()) == false){
			UpdatePasswordRequest updatePassword = new UpdatePasswordRequest();
			updatePassword.setLoginName(employeeAddRequest.getLoginName());
			updatePassword.setOldPassword(employeeEntity.getPassword());
			updatePassword.setNewPassword(employeeAddRequest.getPassword());
			try {
				boolean isSuccess = resetPassword(updatePassword,token);
				if(isSuccess == false){
					logger.error("updatepassword to remote server failed!");
		            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("updatepassword to remote server failed!",e);
	            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		if(null != employeeAddRequest.getRoleCodes() && employeeAddRequest.getRoleCodes().isEmpty() == false){
			UpdateUserRoleRequest updateUserRoleRequest = new UpdateUserRoleRequest();
			updateUserRoleRequest.setLoginName(employeeAddRequest.getLoginName());
			updateUserRoleRequest.setRoleCodes(employeeAddRequest.getRoleCodes());
			try {
				boolean isSuccess = updateUserRoles(updateUserRoleRequest,token);
				if(isSuccess == false){
					logger.error("updateUserRoles to remote server failed!");
		            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("updateUserRoles to remote server failed!",e);
	            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		BeanUtils.copyPropertiesIgnoreNull(employeeAddRequest, employeeEntity);
		employeeRepository.save(employeeEntity);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	
	private boolean resetPassword(UpdatePasswordRequest updatePassword,String token) throws Exception {
		RestTemplateResponseErrorHandler responseErrorHandler = new RestTemplateResponseErrorHandler();
		boolean isSuccessFlag = false;
		try{
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.put("Content-Type",
					Lists.newArrayList(MediaType.APPLICATION_JSON_VALUE));
			headers.put(SECURITY_TOKEN_HEADER, Lists.newArrayList(token));
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setErrorHandler(responseErrorHandler);
			ResponseEntity responseBody =restTemplate.exchange(env.getRequiredProperty("updatePassword.endpoint"),
					HttpMethod.POST, new HttpEntity<>(updatePassword,
							headers), ResponseEntity.class);
			if(responseBody.getStatusCode().equals(HttpStatus.OK)){
				isSuccessFlag = true; 
			}
		}catch(Exception e){
			logger.error("update to remote server failed!",e);
			return false;
		}
		return isSuccessFlag;
	}
	
	private boolean updateUserRoles(UpdateUserRoleRequest updateUserRoleRequest,String token) throws Exception {
		RestTemplateResponseErrorHandler responseErrorHandler = new RestTemplateResponseErrorHandler();
		boolean isSuccessFlag = false;
		try{
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.put("Content-Type",
					Lists.newArrayList(MediaType.APPLICATION_JSON_VALUE));
			headers.put(SECURITY_TOKEN_HEADER, Lists.newArrayList(token));
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setErrorHandler(responseErrorHandler);
			ResponseEntity responseBody =restTemplate.exchange(env.getRequiredProperty("updateUserRoles.endpoint"),
					HttpMethod.POST, new HttpEntity<>(updateUserRoleRequest,
							headers), ResponseEntity.class);
			if(responseBody.getStatusCode().equals(HttpStatus.OK)){
				isSuccessFlag = true; 
			}
		}catch(Exception e){
			logger.error("updateUserRoles to remote server failed!",e);
			return false;
		}
		return isSuccessFlag;
	}
}
