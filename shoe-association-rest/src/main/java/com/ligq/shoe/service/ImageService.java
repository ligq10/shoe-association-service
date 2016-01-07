package com.ligq.shoe.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ligq.shoe.constants.ImageType;
import com.ligq.shoe.controller.FileController;
import com.ligq.shoe.controller.ShoeCompanyController;
import com.ligq.shoe.entity.Image;
import com.ligq.shoe.repository.ImageRepository;
import com.ligq.shoe.utils.FileUtils;

@Service
public class ImageService {

	private final static Logger logger = LoggerFactory.getLogger(ImageService.class); 
	
	@Autowired
	private ImageRepository imageRepository;
	
	public ResponseEntity<?> save(
			MultipartFile file,
			HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		ResponseEntity<Object> responseEntity = null;
		Image image = saveFile(file);
		if(null == image){
			return  new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);				
		}
        HttpHeaders headers = new HttpHeaders();
		headers.setLocation(linkTo(methodOn(FileController.class).findOneImage(image.getUuid(), request,response)).toUri());
		responseEntity = new ResponseEntity<Object>(image,headers,HttpStatus.CREATED);				

		return responseEntity;  
	}

	private Image saveFile(MultipartFile file){
		Image image = new Image();
		//获取存储路径
		File fileBaseDir = FileUtils.createTempDir();
		//获取文件名
		String fileName = file.getOriginalFilename();
        //获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名  
        String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());  
        if(isImage(ext)==false){
			logger.error("image type is not support");
			return null;			      	
        }
        //重新命名文件名
        String newFileName = System.currentTimeMillis()+Math.round(Math.random()*9)+"."+ext;
		
        FileOutputStream os = null;
		//拿到上传文件的输入流  
		FileInputStream in = null;
		try {
			File newFile = new File(fileBaseDir,newFileName);
			String newFilePath = newFile.getAbsolutePath();
			os = new FileOutputStream(newFilePath);
			in = (FileInputStream) file.getInputStream();  
			  
			//以写字节的方式写文件  
			int b = 0;  
			while((b=in.read()) != -1){  
			    os.write(b);  
			}
			
			image.setUuid(UUID.randomUUID().toString());
			image.setPath(newFilePath);
			image.setCreateTime(new Date());
			image.setMimeType(file.getContentType());
			image.setName(fileName);
			image = imageRepository.save(image);
			return image;
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(),e);
			return null;			      	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			return null;			      	
		}finally{
			if(null != os){
		        try {
					os.flush();
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage(),e);
				}  
		        
			}
			if(null != in){
		        try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage(),e);
				} 					
			}
		}
	}
	
	private boolean isImage(String mimeType){
		
		for(ImageType imagetype : ImageType.values()){
			if(mimeType.equalsIgnoreCase(imagetype.getMimetype())){
				return true;
			}
		}
		return false;
	}

	public Image findOne(String uuid) {
		// TODO Auto-generated method stub
		Image image = imageRepository.findOne(uuid);

		return image;
	}

	public ResponseEntity<?> save(MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		List<String> fileIds = new ArrayList<String>();
		for(MultipartFile file : files){
			Image image = this.saveFile(file);
			if(null != image){
				fileIds.add(image.getUuid());
			}
		}
		return new ResponseEntity<Object>(fileIds,HttpStatus.CREATED);				
	}
}
