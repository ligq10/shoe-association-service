package com.ligq.shoe.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.util.UUID;

import javax.servlet.annotation.MultipartConfig;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.entity.Employee;
import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.model.ShoeCompanyAddRequest;
import com.ligq.shoe.model.ShoeCompanyResponse;
import com.ligq.shoe.service.ShoeCompanyService;
import com.ligq.shoe.service.EmployeeService;
import com.ligq.shoe.validator.AddShoeCompanyValidator;
import com.ligq.shoe.constants.CheckCodeType;
import com.ligq.shoe.constants.CreditLevel;

@Controller
public class ShoeCompanyController {

	private final static Logger logger = LoggerFactory.getLogger(ShoeCompanyController.class); 

	@Autowired
	private ShoeCompanyService shoeCompanyService;
	
	@Autowired
	private EmployeeService userService;
	
	@Autowired
	private AddShoeCompanyValidator addShoeCompanyValidator;
	
	@RequestMapping(value="/shoecompanies",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> saveShoeCompany(
			@RequestBody ShoeCompanyAddRequest shoeCompanyAddRequest,
			HttpServletRequest request,
			HttpServletResponse response,
			BindingResult result){
		
		addShoeCompanyValidator.validate(shoeCompanyAddRequest, result);
		if(result.hasErrors()){
			logger.error("Add Shoe Company validation failed:"+result);
			throw new RepositoryConstraintViolationException(result);
		}
		HttpSession session = request.getSession();
		SendMsg sendMsg = (SendMsg) session.getAttribute(shoeCompanyAddRequest.getTel());
		if(null == sendMsg){
			return new ResponseEntity<Object>("验证码无效，请检查!",HttpStatus.BAD_REQUEST);			
		}
		String checkCode = sendMsg.getCheckCode();
		if(sendMsg.getType().equals(CheckCodeType.REGISTER.getValue()) == false 
				|| checkCode.equals(shoeCompanyAddRequest.getCheckCode()) == false){
			return new ResponseEntity<Object>("验证码错误，请检查!",HttpStatus.BAD_REQUEST);			
		}
		session.removeAttribute(shoeCompanyAddRequest.getTel());
		ResponseEntity<Object> responseEntity =  null;		
		try {	        
	        responseEntity=shoeCompanyService.save(shoeCompanyAddRequest,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage(),e);
			responseEntity=new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;
	}
	
	@RequestMapping(value="/shoecompanies/{uuid}",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findOneShoeCompanyById(
			 @PathVariable String uuid,
			 HttpServletRequest request,
			 HttpServletResponse response){
		ShoeCompany shoeCompany = shoeCompanyService.findOneShoeCompanyById(uuid);
		if(null == shoeCompany){
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		
		ShoeCompanyResponse shoeCompanyResponse = new ShoeCompanyResponse();
		BeanUtils.copyProperties(shoeCompany, shoeCompanyResponse);
	    Link selfLink = linkTo(methodOn(this.getClass()).findOneShoeCompanyById(shoeCompany.getUuid(), request, response)).withSelfRel();	    
	    String logoImageUrl = shoeCompanyService.getHost(request)+"/images/show/"+shoeCompany.getLogoImageId();
	    shoeCompanyResponse.setLogoImageUrl(logoImageUrl);
	    String permitImageUrl = shoeCompanyService.getHost(request)+"/images/show/"+shoeCompany.getPermitImageId();
	    shoeCompanyResponse.setPermitImageUrl(permitImageUrl);;
	    shoeCompanyResponse.setTotalScore(shoeCompany.getCreditScore()+shoeCompany.getQualityScore()+shoeCompany.getServeScore());
	    shoeCompanyResponse.setCreditDesc(CreditLevel.getCreditDesc(shoeCompany.getCreditLevel()).getDesc());
	    shoeCompanyResponse.add(selfLink);
        return new ResponseEntity<Resource>(new Resource<ShoeCompanyResponse>(shoeCompanyResponse), HttpStatus.OK);		
	}
	
	@RequestMapping(value="/shoecompanies",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findShoeCompany(
			
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "20") int size,
			@RequestParam(value = "auditStatus", required = false, defaultValue = "0") Integer auditStatus,

			 HttpServletRequest request,
			 HttpServletResponse response){
		String name = request.getParameter("name");
		String phoneticize = request.getParameter("phoneticize");
		String level =request.getParameter("level");
		String sortStr =request.getParameter("sort");
		StringBuilder pathParams = new StringBuilder();

		Pageable pageable = null;
		Page<ShoeCompany> shoeCompanyPage = null;
		Sort sort = new Sort(Direction.ASC, "namePhoneticize");
		if(StringUtils.isEmpty(sortStr) == false){
			if(sortStr.equalsIgnoreCase("desc")){
				sort = new Sort(Direction.DESC, "namePhoneticize");
			}else{
				sort = new Sort(Direction.ASC, "namePhoneticize");
			}
			pathParams.append("&sort="+sortStr);

		}
		
		if(StringUtils.isEmpty(auditStatus) == false){
			pathParams.append("&auditStatus"+auditStatus);
		}
		
		if(StringUtils.isEmpty(name) == false){
			pageable = new PageRequest(page, size);
			shoeCompanyPage = shoeCompanyService.findAllShoeCompanyByName(name,auditStatus,pageable);
			pathParams.append("&name="+name);
		}else if(StringUtils.isEmpty(phoneticize) == false){
			pageable = new PageRequest(page, size);
			shoeCompanyPage = shoeCompanyService.findAllShoeCompanybyPhoneticize(phoneticize,auditStatus,pageable);
			pathParams.append("&phoneticize="+phoneticize);
		
		}else if(StringUtils.isEmpty(level) ==false){
			pageable = new PageRequest(page, size,sort);
			shoeCompanyPage = shoeCompanyService.findAllShoeCompanyByCreditLevel(Integer.valueOf(level),auditStatus,pageable);
			pathParams.append("&level="+level);

		}else{
			pageable = new PageRequest(page, size,sort);
			shoeCompanyPage = shoeCompanyService.findAllShoeCompany(auditStatus,pageable);
		}

		
		ResponseEntity responseEntity = null;
		try {
			responseEntity = shoeCompanyService.getResponseEntityConvertShoeCompanyPage(pathParams.toString(),shoeCompanyPage, pageable, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("User Locations Not Found:",e);
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);			
		}
		return  responseEntity;			
	}
	
	
	@RequestMapping(value="/shoecompanies/audit",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findShoeCompanyAudit(
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "auditStatus", required = false, defaultValue = "0") Integer auditStatus,
            @PageableDefault(page = 0, size = 20, sort = "createTime", direction = Sort.Direction.ASC) Pageable pageable,
			 HttpServletRequest request,
			 HttpServletResponse response){
		StringBuilder queryParams = new  StringBuilder();
		Page<ShoeCompany> shoeCompanyPage = null;
		if(StringUtils.isEmpty(keyword) == false){
			queryParams.append("&keyword"+keyword);
		}
		if(StringUtils.isEmpty(auditStatus) == false){
			queryParams.append("&auditStatus"+auditStatus);
		}
		shoeCompanyPage = shoeCompanyService.findAllShoeCompanyAudit(keyword,auditStatus,pageable);
		
		ResponseEntity responseEntity = null;
		try {
			responseEntity = shoeCompanyService.getResponseEntityConvertShoeCompanyPage(queryParams.toString(),shoeCompanyPage, pageable, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("User Locations Not Found:",e);
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);			
		}
		return  responseEntity;			
	}

	@RequestMapping(value="/shoecompanies/withoutaudit",method = RequestMethod.POST, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> addShoeCompanyWithoutAudit(
			@RequestBody ShoeCompanyAddRequest shoeCompanyAddRequest,
			 HttpServletRequest request,
			 HttpServletResponse response,
			 BindingResult result){
		addShoeCompanyValidator.validate(shoeCompanyAddRequest, result);
		if(result.hasErrors()){
			logger.error("Add Shoe Company validation failed:"+result);
			throw new RepositoryConstraintViolationException(result);
		}

		ResponseEntity<Object> responseEntity =  null;		
		try {	        
	        responseEntity=shoeCompanyService.saveWithoutAudit(shoeCompanyAddRequest,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage(),e);
			responseEntity=new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;		
	}

	@RequestMapping(value="/shoecompanies/{uuid}",method = RequestMethod.PATCH, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> updateShoeCompanyWithoutAudit(	
			 @PathVariable String uuid,			
			 @RequestBody ShoeCompanyAddRequest shoeCompanyAddRequest,
			 HttpServletRequest request,
			 HttpServletResponse response,
			 BindingResult result){
		addShoeCompanyValidator.validate(shoeCompanyAddRequest, result);
		if(result.hasErrors()){
			logger.error("Add Shoe Company validation failed:"+result);
			throw new RepositoryConstraintViolationException(result);
		}

		ResponseEntity<Object> responseEntity =  null;		
		try {	        
	        responseEntity=shoeCompanyService.update(uuid,shoeCompanyAddRequest,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage(),e);
			responseEntity=new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;		
	}
	
	@RequestMapping(value="/shoecompanies/withoutaudit",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findShoeCompanyWithoutAudit(
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 20, sort = "createTime", direction = Sort.Direction.ASC) Pageable pageable,
			 HttpServletRequest request,
			 HttpServletResponse response){
		StringBuilder queryParams = new  StringBuilder();
		Page<ShoeCompany> shoeCompanyPage = null;
		if(StringUtils.isEmpty(keyword) == false){
			queryParams.append("&keyword"+keyword);
		}
		shoeCompanyPage = shoeCompanyService.findByAuditStatus(keyword,pageable);
		
		ResponseEntity responseEntity = null;
		try {
			responseEntity = shoeCompanyService.getResponseEntityConvertShoeCompanyPage(queryParams.toString(),shoeCompanyPage, pageable, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("User Locations Not Found:",e);
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);			
		}
		return  responseEntity;			
	}
	
	@RequestMapping(value="/shoecompanies/{uuid}",method = RequestMethod.DELETE, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> deleteShoeCompanyById(
			 @PathVariable String uuid,
			 HttpServletRequest request,
			 HttpServletResponse response){

		ResponseEntity<?> responseEntity =  null;		
		try {	        
	        responseEntity=shoeCompanyService.delete(uuid,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage(),e);
			responseEntity=new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;
	}
}
