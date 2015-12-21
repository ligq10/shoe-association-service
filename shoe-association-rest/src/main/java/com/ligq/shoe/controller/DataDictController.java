package com.ligq.shoe.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.ligq.shoe.entity.DataDict;
import com.ligq.shoe.entity.DataDictType;
import com.ligq.shoe.model.DataDictAddRequest;
import com.ligq.shoe.model.DataDictResponse;
import com.ligq.shoe.model.DataDictTypeAddRequest;
import com.ligq.shoe.model.DataDictTypeResponse;
import com.ligq.shoe.service.DataDictService;


@Controller
public class DataDictController {

	private final static Logger logger = LoggerFactory.getLogger(DataDictController.class); 

	@Autowired
	private DataDictService dataDictService;
	
	@RequestMapping(value="/datadicts",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> saveDataDict(@RequestBody DataDictAddRequest dataDictAddRequest,
			HttpServletRequest request,
			HttpServletResponse response,
			BindingResult result){
				
		ResponseEntity<Object> responseEntity =  null;		
		try {	        
	        responseEntity=dataDictService.save(dataDictAddRequest,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage());
			responseEntity=new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;
	}
	
	@RequestMapping(value="/datadicts/{uuid}",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findOneDataDictById(
			 @PathVariable String uuid,
			 HttpServletRequest request,
			 HttpServletResponse response){
		DataDict dataDictEntity = dataDictService.findOneDataDictById(uuid);
		if(null == dataDictEntity){
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		DataDictResponse dataDictResponse = new DataDictResponse();
		BeanUtils.copyProperties(dataDictEntity, dataDictResponse);
	    Link selfLink = linkTo(methodOn(this.getClass()).findOneDataDictById(dataDictEntity.getUuid(), request, response)).withSelfRel();	    
	    dataDictResponse.add(selfLink);
        return new ResponseEntity<Resource>(new Resource<DataDictResponse>(dataDictResponse), HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/datadictypes",method=RequestMethod.POST, produces = "application/hal+json")
	public HttpEntity<?> saveDataDictType(@RequestBody DataDictTypeAddRequest dataDictTypeAddRequest,
			HttpServletRequest request,
			HttpServletResponse response,
			BindingResult result){
				
		ResponseEntity<HttpStatus> responseEntity =  null;		
		try {	        
	        responseEntity=dataDictService.save(dataDictTypeAddRequest,request,response);			
		} catch (Exception e) {			
			logger.error(e.getMessage());
			responseEntity=new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);			
		}	
		
		return responseEntity;
	}
	
	@RequestMapping(value="/datadictypes/{uuid}",method = RequestMethod.GET, produces = "application/hal+json;charset=utf-8")
	@Transactional
	public HttpEntity<?> findOneDataDictTypeById(
			 @PathVariable String uuid,
			 HttpServletRequest request,
			 HttpServletResponse response){
		DataDictType dataDictTypeEntity = dataDictService.findOneDataDictTypeById(uuid);
		if(null == dataDictTypeEntity){
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		DataDictTypeResponse dataDictTypeResponse = new DataDictTypeResponse();
		BeanUtils.copyProperties(dataDictTypeEntity, dataDictTypeResponse);
	    Link selfLink = linkTo(methodOn(this.getClass()).findOneDataDictTypeById(dataDictTypeEntity.getUuid(), request, response)).withSelfRel();	    
	    dataDictTypeResponse.add(selfLink);
        return new ResponseEntity<Resource>(new Resource<DataDictTypeResponse>(dataDictTypeResponse), HttpStatus.OK);
		
	}
}
