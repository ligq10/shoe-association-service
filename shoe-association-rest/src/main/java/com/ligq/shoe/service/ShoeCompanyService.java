package com.ligq.shoe.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.ligq.shoe.controller.DataDictController;
import com.ligq.shoe.controller.FileController;
import com.ligq.shoe.controller.ShoeCompanyController;
import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.entity.User;
import com.ligq.shoe.model.CreditLevel;
import com.ligq.shoe.model.NumberToChinese;
import com.ligq.shoe.model.ShoeCompanyAddRequest;
import com.ligq.shoe.model.ShoeCompanyResponse;
import com.ligq.shoe.repository.ShoeCompanyRepository;
import com.ligq.shoe.repository.UserRepository;
import com.ligq.shoe.utils.Pinyin4jUtil;

@Service
public class ShoeCompanyService {

	private final static Logger logger = LoggerFactory.getLogger(ShoeCompanyService.class); 
	
	@Autowired
	private ShoeCompanyRepository shoeCompanyRepository;
	@Autowired
	private UserRepository userRepository;
	
	public ResponseEntity<Object> save(
			ShoeCompanyAddRequest shoeCompanyAddRequest,
			HttpServletRequest request, HttpServletResponse response) {
		if(StringUtils.isEmpty(shoeCompanyAddRequest.getName())){
			logger.error("name is empty");
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		
		User userEntity = new User();
		userEntity.setUuid(UUID.randomUUID().toString());
		userEntity.setName(shoeCompanyAddRequest.getSubmitPerson());
		userEntity.setTel(shoeCompanyAddRequest.getTel());
		userEntity = userRepository.save(userEntity);
		
		ShoeCompany shoeCompany = new ShoeCompany();
		BeanUtils.copyProperties(shoeCompanyAddRequest, shoeCompany);
		Date createTime = new Date();
		shoeCompany.setUuid(UUID.randomUUID().toString());
		shoeCompany.setCreateTime(createTime);
		shoeCompany.setUpdateTime(createTime);
		shoeCompany.setCreditScore(40);
		shoeCompany.setQualityScore(30);
		shoeCompany.setServeScore(30);
		shoeCompany.setCreditLevel(0);
		shoeCompany.setSubmitPersonId(userEntity.getUuid());
		String firstPinyin = getfirstSpellByChineseCharacter(shoeCompanyAddRequest.getName());
		shoeCompany.setNamePhoneticize(firstPinyin);
		shoeCompany = shoeCompanyRepository.save(shoeCompany);
		
		HttpHeaders headers = new HttpHeaders();

		URI selfUrl = linkTo(methodOn(ShoeCompanyController.class).findOneShoeCompanyById(shoeCompany.getUuid(), request, response)).toUri();
		headers.setLocation(selfUrl);
		return new ResponseEntity<Object>(headers,HttpStatus.CREATED);

	}
	
	private String getfirstSpellByChineseCharacter(String characters){
    	String firstPinyin = ""; 
		if(StringUtils.isEmpty(characters)){
			return firstPinyin;
		}	
		         
        char[] nameChar = characters.toCharArray();  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);  
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        String Char = String.valueOf(nameChar[0]);
    	if (isChineseChar(Char)) {              	
            //String pinyinName = PinyinHelper.toHanyuPinyinStringArray(nameChar[0], defaultFormat)[0];
			//char[] pinyinChar = pinyinName.toCharArray();  
			firstPinyin = Pinyin4jUtil.getFirstPinyinAndUpperCase(Char);
        }else if(isNumber(Char)){
        	String chineseNumber = NumberToChinese.getNumberToChineseByValue(Integer.valueOf(Char)).getChinese();
        	firstPinyin = Pinyin4jUtil.getFirstPinyinAndUpperCase(chineseNumber);;
        }else{
        	firstPinyin = Char.toUpperCase();
        }  
	    return firstPinyin;
	             		        
	}
	
	public boolean isNumber(String str){
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();  
	}
	
    public boolean isChineseChar(String str){
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
        Matcher m=p.matcher(str); 
        if(m.find()){ 
        	return  true;
        }
        return false;
    }

	public ShoeCompany findOneShoeCompanyById(String uuid) {
		// TODO Auto-generated method stub
		ShoeCompany shoeCompany = shoeCompanyRepository.findOne(uuid);
		return shoeCompany;
	}

	public Page<ShoeCompany> findAllShoeCompany(Pageable pageable) {
		// TODO Auto-generated method stub
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findAll(pageable);
		return shoeCompanyPage;
	}

	public Page<ShoeCompany> findAllShoeCompanyByName(String name,
			Pageable pageable) {
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByName(name, pageable);
		return shoeCompanyPage;
	}

	public Page<ShoeCompany> findAllShoeCompanybyPhoneticize(
			String phoneticize, Pageable pageable) {
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByNamePhoneticize(phoneticize, pageable);
		return shoeCompanyPage;
	}
	
	public Page<ShoeCompany> findAllShoeCompanyByCreditLevel(Integer level, Pageable pageable) {
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByCreditLevel(level, pageable);
		return shoeCompanyPage;
	}
	
	public ResponseEntity getResponseEntityConvertShoeCompanyPage(String pathParams,Page<ShoeCompany> shoeCompanyResult,
			Pageable pageable,HttpServletRequest request,HttpServletResponse response)throws Exception{

		List<Link> list = prepareLinks(pageable.getPageNumber(),
				pageable.getPageSize(), request, shoeCompanyResult,pathParams);
		List<ShoeCompanyResponse> content = new ArrayList<ShoeCompanyResponse>();
		
		if(null != shoeCompanyResult){
			for (ShoeCompany shoeCompany : shoeCompanyResult.getContent()) {

				User user = userRepository.findOne(shoeCompany.getSubmitPersonId());		
				ShoeCompanyResponse shoeCompanyResponse = new ShoeCompanyResponse();
				BeanUtils.copyProperties(shoeCompany, shoeCompanyResponse);
			    Link selfLink = linkTo(methodOn(ShoeCompanyController.class).findOneShoeCompanyById(shoeCompany.getUuid(), request, response)).withSelfRel();	    
			    String logoImageUrl = getHost(request)+"/images/show/"+shoeCompany.getLogoImageId();
			    shoeCompanyResponse.setLogoImageUrl(logoImageUrl);
			    String permitImageUrl = getHost(request)+"/images/show/"+shoeCompany.getPermitImageId();
			    shoeCompanyResponse.setPermitImageUrl(permitImageUrl);;
			    shoeCompanyResponse.setTotalScore(shoeCompany.getCreditScore()+shoeCompany.getQualityScore()+shoeCompany.getServeScore());
			    shoeCompanyResponse.setSubmitPerson(user.getName());
			    shoeCompanyResponse.setTel(user.getTel());
			    shoeCompanyResponse.setCreditDesc(CreditLevel.getCreditDesc(shoeCompany.getCreditLevel()).getDesc());

			    shoeCompanyResponse.add(selfLink);
				content.add(shoeCompanyResponse);
			}			
		}
		
		PagedResources<ShoeCompanyResponse> pagedResources = new PagedResources<ShoeCompanyResponse>(
				content, new PageMetadata(shoeCompanyResult.getSize(), shoeCompanyResult.getNumber(),
						shoeCompanyResult.getTotalElements(), shoeCompanyResult.getTotalPages()),
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


	
}
