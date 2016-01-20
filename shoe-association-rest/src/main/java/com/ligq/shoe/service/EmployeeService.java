package com.ligq.shoe.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ligq.shoe.controller.EmployeeContorller;
import com.ligq.shoe.entity.Employee;
import com.ligq.shoe.model.EmployeeAddRequest;
import com.ligq.shoe.model.EmployeeResponse;
import com.ligq.shoe.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	public  Employee findOneUserById(String uuid){
		Employee employeeEntity = employeeRepository.findOne(uuid);
		return employeeEntity;
	}

	public ResponseEntity<Object> save(EmployeeAddRequest employeeAddRequest,
			HttpServletRequest request, HttpServletResponse response) {
		Employee employeeEntity = new Employee();
		BeanUtils.copyProperties(employeeAddRequest, employeeEntity);
		Date createTime = new Date();
		employeeEntity.setUuid(UUID.randomUUID().toString());
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

		List<Link> list = prepareLinks(pageable.getPageNumber(),
				pageable.getPageSize(), request, employeeResult,pathParams);
		List<EmployeeResponse> content = new ArrayList<EmployeeResponse>();
		
		if(null != employeeResult){
			for (Employee employee : employeeResult.getContent()) {

				EmployeeResponse employeeResponse = new EmployeeResponse();
				BeanUtils.copyProperties(employee, employeeResponse);
			    Link selfLink = linkTo(methodOn(EmployeeContorller.class).findOneEmployeeById(employee.getUuid(), request, response)).withSelfRel();	    
			    employeeResponse.add(selfLink);
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


}
