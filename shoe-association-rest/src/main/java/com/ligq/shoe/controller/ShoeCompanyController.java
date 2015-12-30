package com.ligq.shoe.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.util.UUID;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import org.springframework.web.multipart.MultipartFile;


import com.ligq.shoe.entity.DataDictType;
import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.entity.User;
import com.ligq.shoe.model.CreditLevel;
import com.ligq.shoe.model.DataDictAddRequest;
import com.ligq.shoe.model.DataDictTypeResponse;
import com.ligq.shoe.model.ShoeCompanyAddRequest;
import com.ligq.shoe.model.ShoeCompanyResponse;
import com.ligq.shoe.service.ShoeCompanyService;
import com.ligq.shoe.service.UserService;
import com.ligq.shoe.controller.FileController;;
@Controller
public class ShoeCompanyController {

	private final static Logger logger = LoggerFactory.getLogger(ShoeCompanyController.class); 

	@Autowired
	private ShoeCompanyService shoeCompanyService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/shoecompanies",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> saveShoeCompany(
			@RequestBody ShoeCompanyAddRequest shoeCompanyAddRequest,
			HttpServletRequest request,
			HttpServletResponse response){
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
		
		User user = userService.findOneUserById(shoeCompany.getSubmitPersonId());		
		ShoeCompanyResponse shoeCompanyResponse = new ShoeCompanyResponse();
		BeanUtils.copyProperties(shoeCompany, shoeCompanyResponse);
	    Link selfLink = linkTo(methodOn(this.getClass()).findOneShoeCompanyById(shoeCompany.getUuid(), request, response)).withSelfRel();	    
	    String logoImageUrl = shoeCompanyService.getHost(request)+"/images/show/"+shoeCompany.getLogoImageId();
	    shoeCompanyResponse.setLogoImageUrl(logoImageUrl);
	    String permitImageUrl = shoeCompanyService.getHost(request)+"/images/show/"+shoeCompany.getPermitImageId();
	    shoeCompanyResponse.setPermitImageUrl(permitImageUrl);;
	    shoeCompanyResponse.setTotalScore(shoeCompany.getCreditScore()+shoeCompany.getQualityScore()+shoeCompany.getServeScore());
	    shoeCompanyResponse.setSubmitPerson(user.getName());
	    shoeCompanyResponse.setTel(user.getTel());
	    shoeCompanyResponse.setCreditDesc(CreditLevel.getCreditDesc(shoeCompany.getCreditLevel()).getDesc());
	    shoeCompanyResponse.add(selfLink);
        return new ResponseEntity<Resource>(new Resource<ShoeCompanyResponse>(shoeCompanyResponse), HttpStatus.OK);		
	}
	
	@RequestMapping(value="/shoecompanies",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findShoeCompany(
			
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "20") int size,
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
		
		if(StringUtils.isEmpty(name) == false){
			pageable = new PageRequest(page, size);
			shoeCompanyPage = shoeCompanyService.findAllShoeCompanyByName(name,pageable);
			pathParams.append("&name="+name);
		}else if(StringUtils.isEmpty(phoneticize) == false){
			pageable = new PageRequest(page, size);
			shoeCompanyPage = shoeCompanyService.findAllShoeCompanybyPhoneticize(phoneticize,pageable);
			pathParams.append("&phoneticize="+phoneticize);
		
		}else if(StringUtils.isEmpty(level) ==false){
			pageable = new PageRequest(page, size,sort);
			shoeCompanyPage = shoeCompanyService.findAllShoeCompanyByCreditLevel(Integer.valueOf(level),pageable);
			pathParams.append("&level="+level);

		}else{
			pageable = new PageRequest(page, size,sort);
			shoeCompanyPage = shoeCompanyService.findAllShoeCompany(pageable);
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
}
