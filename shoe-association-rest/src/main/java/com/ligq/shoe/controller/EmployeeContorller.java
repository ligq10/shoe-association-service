package com.ligq.shoe.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ligq.shoe.constants.CheckCodeType;
import com.ligq.shoe.constants.CreditLevel;
import com.ligq.shoe.entity.Employee;
import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.model.EmployeeAddRequest;
import com.ligq.shoe.model.EmployeeResponse;
import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.model.ShoeCompanyAddRequest;
import com.ligq.shoe.model.ShoeCompanyResponse;
import com.ligq.shoe.service.EmployeeService;
import com.ligq.shoe.validator.AddEmployeeValidator;

@Controller
public class EmployeeContorller {

	private final static Logger logger = LoggerFactory.getLogger(EmployeeContorller.class); 

	@Autowired
	private AddEmployeeValidator addEmployeeValidator;
	
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value="/employees",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> addEmployee(
			@RequestBody EmployeeAddRequest employeeAddRequest,
			HttpServletRequest request,
			HttpServletResponse response,
			BindingResult result){
		
		addEmployeeValidator.validate(employeeAddRequest, result);
		if(result.hasErrors()){
			logger.error("Add Employee validation failed:"+result);
			throw new RepositoryConstraintViolationException(result);
		}

		ResponseEntity<Object> responseEntity =  null;		
		try {	        
	        responseEntity=employeeService.save(employeeAddRequest,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage(),e);
			responseEntity=new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;
	}

	
	@RequestMapping(value="/employees/{uuid}",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findOneEmployeeById(
			 @PathVariable String uuid,
			 HttpServletRequest request,
			 HttpServletResponse response){
		Employee employeeEntity = employeeService.findOneUserById(uuid);
		if(null == employeeEntity){
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		
		EmployeeResponse employeeResponse = new EmployeeResponse();
		BeanUtils.copyProperties(employeeEntity, employeeResponse);
	    Link selfLink = linkTo(methodOn(this.getClass()).findOneEmployeeById(employeeEntity.getUuid(), request, response)).withSelfRel();	    
	    employeeResponse.add(selfLink);
        return new ResponseEntity<Resource>(new Resource<EmployeeResponse>(employeeResponse), HttpStatus.OK);		
	}
	
	@RequestMapping(value="/employees",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findEmployee(
			@RequestParam(value = "keyword", required = true, defaultValue = "") String keyword,
			@PageableDefault(page = 0, size = 20, sort = "createTime", direction = Direction.DESC) Pageable pageRequest,
			 HttpServletRequest request,
			 HttpServletResponse response){

		Page<Employee> employeePage = null;
		employeePage = employeeService.findAllEmployeeByKeword(keyword,pageRequest);
		
		ResponseEntity responseEntity = null;
		try {
			responseEntity = employeeService.getResponseEntityConvertEmployeePage("",employeePage, pageRequest, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("User Locations Not Found:",e);
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);			
		}
		return  responseEntity;			
	}
}
