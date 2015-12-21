package com.ligq.shoe.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.Date;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.ligq.shoe.controller.DataDictController;
import com.ligq.shoe.controller.ShoeCompanyController;
import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.entity.User;
import com.ligq.shoe.model.ShoeCompanyAddRequest;
import com.ligq.shoe.repository.ShoeCompanyRepository;
import com.ligq.shoe.repository.UserRepository;

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
            try {
            	String pinyinName = PinyinHelper.toHanyuPinyinStringArray(nameChar[0], defaultFormat)[0];
                char[] pinyinChar = pinyinName.toCharArray();  
                firstPinyin = String.valueOf(pinyinChar[0]);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
            	logger.error(e.getMessage(),e);
            	firstPinyin="";
			}
        }else{
        	firstPinyin = Char;
        }  
	    return firstPinyin;
	             		        
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
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByNameLike(name, pageable);
		return shoeCompanyPage;
	}

	public Page<ShoeCompany> findAllShoeCompanybyPhoneticize(
			String phoneticize, Pageable pageable) {
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByNamePhoneticizeLike(phoneticize, pageable);
		return shoeCompanyPage;
	}
}
