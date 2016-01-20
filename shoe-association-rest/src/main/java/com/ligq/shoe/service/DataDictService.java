package com.ligq.shoe.service;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.ligq.shoe.controller.DataDictController;
import com.ligq.shoe.entity.DataDict;
import com.ligq.shoe.entity.DataDictType;
import com.ligq.shoe.model.DataDictAddRequest;
import com.ligq.shoe.model.DataDictTypeAddRequest;
import com.ligq.shoe.repository.DataDictRepository;
import com.ligq.shoe.repository.DataDictTypeRepository;

@Service
public class DataDictService {

	private final static Logger logger = LoggerFactory.getLogger(DataDictService.class); 

	
	@Autowired
	private DataDictRepository dataDictRepository;
	
	@Autowired
	private DataDictTypeRepository dataDictTypeRepository;
	
	public ResponseEntity save(DataDictAddRequest dataDictAddRequest,
			HttpServletRequest request,
			HttpServletResponse response){
		if(StringUtils.isEmpty(dataDictAddRequest.getDictCode()) || StringUtils.isEmpty(dataDictAddRequest.getTypeCode())){
			logger.error("DictCode is empty");
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		
		DataDictType dataDictType = dataDictTypeRepository.findByTypeCode(dataDictAddRequest.getTypeCode());
		if(null == dataDictType){
			logger.error("typecode not exist");
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		
		List<DataDict> dataDictList = dataDictRepository.findByTypeCodeAndDictCode(dataDictAddRequest.getTypeCode(),dataDictAddRequest.getDictCode());
		if(null == dataDictList || dataDictList.isEmpty()){
			DataDict dataDict = new DataDict();
			BeanUtils.copyProperties(dataDictAddRequest, dataDict);
			dataDict.setUuid(UUID.randomUUID().toString());
			dataDict.setTypeId(dataDictType.getUuid());
			dataDict = dataDictRepository.save(dataDict);
			HttpHeaders headers = new HttpHeaders();

			URI selfUrl = linkTo(methodOn(DataDictController.class).findOneDataDictById(dataDict.getUuid(), request, response)).toUri();
			headers.setLocation(selfUrl);
			return new ResponseEntity<HttpStatus>(headers,HttpStatus.CREATED);
		}else{
			logger.error("DictCode already exist");
			return new ResponseEntity<Object>(HttpStatus.CREATED);
		}
		
	}
	
	public DataDictType save(DataDictTypeAddRequest dataDictAddRequest){
		return null;
	}

	public DataDict findOneDataDictById(String uuid) {
		// TODO Auto-generated method stub
		DataDict dataDict = dataDictRepository.findOne(uuid);
		return dataDict;
	}

	public ResponseEntity<HttpStatus> save(
			DataDictTypeAddRequest dataDictTypeAddRequest,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		if(StringUtils.isEmpty(dataDictTypeAddRequest.getTypeCode())){
			logger.error("TypeCode is empty");
			return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
		DataDictType dataDictType = dataDictTypeRepository.findByTypeCode(dataDictTypeAddRequest.getTypeCode());
		if(null == dataDictType ){
			DataDictType dataDictTypeEntity = new DataDictType();
			BeanUtils.copyProperties(dataDictTypeAddRequest, dataDictTypeEntity);
			dataDictTypeEntity.setUuid(UUID.randomUUID().toString());
			dataDictTypeEntity = dataDictTypeRepository.save(dataDictTypeEntity);
			HttpHeaders headers = new HttpHeaders();

			URI selfUrl = linkTo(methodOn(DataDictController.class).findOneDataDictTypeById(dataDictTypeEntity.getUuid(), request, response)).toUri();
			headers.setLocation(selfUrl);
			return new ResponseEntity<HttpStatus>(headers,HttpStatus.CREATED);
		}else{
			logger.error("DictType already exist");
			return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
		}
		
	}

	public DataDictType findOneDataDictTypeById(String uuid) {
		// TODO Auto-generated method stub
		DataDictType dataDictType = dataDictTypeRepository.findOne(uuid);
		return dataDictType;
	}
}
