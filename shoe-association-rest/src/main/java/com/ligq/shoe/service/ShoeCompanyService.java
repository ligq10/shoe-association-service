package com.ligq.shoe.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.File;
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

import com.ligq.shoe.constants.CreditLevel;
import com.ligq.shoe.constants.NumberToChinese;
import com.ligq.shoe.constants.ShoeCompanyAuditStatus;
import com.ligq.shoe.controller.DataDictController;
import com.ligq.shoe.controller.FileController;
import com.ligq.shoe.controller.ShoeCompanyController;
import com.ligq.shoe.entity.Image;
import com.ligq.shoe.entity.ShoeCompany;
import com.ligq.shoe.entity.Employee;
import com.ligq.shoe.model.ShoeCompanyAddRequest;
import com.ligq.shoe.model.ShoeCompanyResponse;
import com.ligq.shoe.repository.ImageRepository;
import com.ligq.shoe.repository.ShoeCompanyRepository;
import com.ligq.shoe.repository.EmployeeRepository;
import com.ligq.shoe.utils.Pinyin4jUtil;

@Service
public class ShoeCompanyService {

	private final static Logger logger = LoggerFactory.getLogger(ShoeCompanyService.class); 
	
	@Autowired
	private ShoeCompanyRepository shoeCompanyRepository;
	@Autowired
	private EmployeeRepository userRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	public ResponseEntity<Object> save(
			ShoeCompanyAddRequest shoeCompanyAddRequest,
			HttpServletRequest request, HttpServletResponse response) {
		if(StringUtils.isEmpty(shoeCompanyAddRequest.getName())){
			logger.error("name is empty");
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		
		
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
		shoeCompany.setAuditStatus(ShoeCompanyAuditStatus.WAITING_AUDIT.getValue());
		String firstPinyin = getfirstSpellByChineseCharacter(shoeCompanyAddRequest.getName());
		shoeCompany.setNamePhoneticize(firstPinyin);
		shoeCompany = shoeCompanyRepository.save(shoeCompany);
		
		HttpHeaders headers = new HttpHeaders();

		URI selfUrl = linkTo(methodOn(ShoeCompanyController.class).findOneShoeCompanyById(shoeCompany.getUuid(), request, response)).toUri();
		headers.setLocation(selfUrl);
		return new ResponseEntity<Object>(headers,HttpStatus.CREATED);

	}
	
	public ResponseEntity<Object> saveWithoutAudit(
			ShoeCompanyAddRequest shoeCompanyAddRequest,
			HttpServletRequest request, HttpServletResponse response) {
		if(StringUtils.isEmpty(shoeCompanyAddRequest.getName())){
			logger.error("name is empty");
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		
		
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
		shoeCompany.setAuditStatus(ShoeCompanyAuditStatus.PASS_AUDIT.getValue());
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

	public Page<ShoeCompany> findAllShoeCompany(Integer auditStatus,Pageable pageable) {
		// TODO Auto-generated method stub
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByAuditStatus(auditStatus,pageable);
		return shoeCompanyPage;
	}

	public Page<ShoeCompany> findAllShoeCompanyAudit(String name,Integer auditStatus,Pageable pageable) {
		// TODO Auto-generated method stub
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByKeywordAndAuditStatus(name,auditStatus,pageable);
		return shoeCompanyPage;
	}
	
	public Page<ShoeCompany> findAllShoeCompanyByName(String name,Integer auditStatus,
			Pageable pageable) {
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByNameAndAuditStatus(name,auditStatus, pageable);
		return shoeCompanyPage;
	}

	public Page<ShoeCompany> findAllShoeCompanybyPhoneticize(
			String phoneticize,Integer auditStatus, Pageable pageable) {
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByNamePhoneticizeAndAuditStatus(phoneticize,auditStatus, pageable);
		return shoeCompanyPage;
	}
	
	public Page<ShoeCompany> findAllShoeCompanyByCreditLevel(Integer level,Integer auditStatus, Pageable pageable) {
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByCreditLevelAndAuditStatus(level,auditStatus, pageable);
		return shoeCompanyPage;
	}
	
	public ResponseEntity getResponseEntityConvertShoeCompanyPage(String pathParams,Page<ShoeCompany> shoeCompanyResult,
			Pageable pageable,HttpServletRequest request,HttpServletResponse response)throws Exception{

		List<Link> list = prepareLinks(pageable.getPageNumber(),
				pageable.getPageSize(), request, shoeCompanyResult,pathParams);
		List<ShoeCompanyResponse> content = new ArrayList<ShoeCompanyResponse>();
		
		if(null != shoeCompanyResult){
			for (ShoeCompany shoeCompany : shoeCompanyResult.getContent()) {

				ShoeCompanyResponse shoeCompanyResponse = new ShoeCompanyResponse();
				BeanUtils.copyProperties(shoeCompany, shoeCompanyResponse);
			    Link selfLink = linkTo(methodOn(ShoeCompanyController.class).findOneShoeCompanyById(shoeCompany.getUuid(), request, response)).withSelfRel();	    
			    String logoImageUrl = getHost(request)+"/images/show/"+shoeCompany.getLogoImageId();
			    shoeCompanyResponse.setLogoImageUrl(logoImageUrl);
			    String permitImageUrl = getHost(request)+"/images/show/"+shoeCompany.getPermitImageId();
			    shoeCompanyResponse.setPermitImageUrl(permitImageUrl);;
			    shoeCompanyResponse.setTotalScore(shoeCompany.getCreditScore()+shoeCompany.getQualityScore()+shoeCompany.getServeScore());
			    shoeCompanyResponse.setCreditDesc(CreditLevel.getCreditDesc(shoeCompany.getCreditLevel()).getDesc());
			    shoeCompanyResponse.setAuditStatusDesc(ShoeCompanyAuditStatus.getShoeCompanyAuditStatus(shoeCompany.getAuditStatus()).getDesc());
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

	public Page<ShoeCompany> findByAuditStatus(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		Page<ShoeCompany> shoeCompanyPage = shoeCompanyRepository.findByKeyword(keyword,pageable);
		
		return shoeCompanyPage;
	}

	public ResponseEntity<?> delete(String uuid, HttpServletRequest request,
			HttpServletResponse response) {
		ShoeCompany shoeCompany = shoeCompanyRepository.findOne(uuid);
		if(null == shoeCompany){
			logger.info("shoeCompany is not exist");
	        return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
		String logoImageId = shoeCompany.getLogoImageId();
		if(StringUtils.isEmpty(logoImageId) == false){
			Image image = imageRepository.findOne(logoImageId);
			deteleFile(image.getPath());
		}
		String permitImageId = shoeCompany.getPermitImageId();
		if(StringUtils.isEmpty(permitImageId)){
			Image image = imageRepository.findOne(permitImageId);
			deteleFile(image.getPath());
		}
		shoeCompanyRepository.delete(shoeCompany);
		return null;
	}
	
	private boolean deteleFile(String filePath){
		    boolean flag = false; 
		    File file = new File(filePath);  
		    // 判断目录或文件是否存在  
		    if (!file.exists()) {  // 不存在返回 false  
		        return flag;  
		    } else {  
		        // 判断是否为文件  
		        if (file.isFile()) {  // 为文件时调用删除文件方法  
		            return deleteFile(file);  
		        } else {  // 为目录时调用删除目录方法  
		            return deleteDirectory(file);  
		        }  
		    }
	}
	
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public boolean deleteFile(File file) {  
	    boolean flag = false; 
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	} 
	
	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */  
	private boolean deleteDirectory(File  dirFile) { 
	    boolean flag = false; 

	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return false;  
	    }  
	    flag = true;  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	            flag = deleteFile(files[i]);  
	            if (!flag) break;  
	        } //删除子目录  
	        else {  
	            flag = deleteDirectory(files[i]);  
	            if (!flag) break;  
	        }  
	    }  
	    if (!flag) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}

	public ResponseEntity<Object> update(String uuid,
			ShoeCompanyAddRequest shoeCompanyAddRequest,
			HttpServletRequest request, HttpServletResponse response) {
		ShoeCompany shoeCompany = shoeCompanyRepository.findOne(uuid);
		if(null == shoeCompany){
			logger.error("shoeCompany is not found");
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
			
		BeanUtils.copyProperties(shoeCompanyAddRequest, shoeCompany);
		Date updateTime = new Date();
		shoeCompany.setUpdateTime(updateTime);
		shoeCompany = shoeCompanyRepository.save(shoeCompany);
	
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
