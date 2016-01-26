package com.ligq.shoe.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ligq.shoe.model.AccessToken;
import com.ligq.shoe.model.AccessTokenInfo;
import com.ligq.shoe.model.EmployeeResponse;
import com.ligq.shoe.model.RoleResponse;
import com.ligq.shoe.service.ViewService;
import com.ligq.shoe.utils.Utils;


@Controller
public class ViewController {

	private final static Logger logger = LoggerFactory.getLogger(ViewController.class); 
	
	private final String SECURITY_TOKEN_HEADER = "X-Token";
	
	@Autowired
	private Environment env;

	private RestTemplate restTemplate = new RestTemplate();	

	@Autowired
	private ViewService viewService;
	
	@RequestMapping(value= "/index")
	public String index(){
		return "shoe_index";
	}

	@RequestMapping(value = "errors")
	public String errors(HttpServletResponse response,HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();  
		for(int i=0;i<cookies.length;i++){  
			Cookie cookie = new Cookie(cookies[i].getName(), null);  
			cookie.setMaxAge(-1);  
			response.addCookie(cookie);  
		}  
		return "error";
	}

	@RequestMapping(value = "expire")
	public ModelAndView expire(@RequestParam String info) {
		Map<String, String> map = Utils.jsonToMap(info);
		ModelAndView mav = new ModelAndView("expire");
		mav.addObject("info", map);
		return mav;
	}
	
	@RequestMapping(value = "loading")
	public String loading(@RequestParam String code,
			HttpServletResponse response, ModelAndView mav) {

		AccessToken accessToken = getAccessToken(code);
		AccessTokenInfo accessTokenInfo = getAccessTokenInfo(accessToken
				.getAccessToken());

		response.addCookie(new Cookie("X-Token", accessToken.getAccessToken()));
		response.addCookie(new Cookie("userName", accessTokenInfo.getUserName()));
		response.addCookie(new Cookie("Authorities", accessTokenInfo
				.getAuthorities().toString()));

		if (accessTokenInfo.getExp() != null) {
			response.addCookie(new Cookie("Token-Exp", accessTokenInfo.getExp()
					.toString()));
		}
		List<String> authorities = accessTokenInfo.getAuthorities();
		List<RoleResponse>  roles = viewService.findRoles(accessToken.getAccessToken());
		if(null == roles || null == authorities || roles.isEmpty() || authorities.isEmpty()){
			return "redirect:/index";
		}else{
			for(RoleResponse role: roles){
				for(String authority : authorities){
					if (authority.equalsIgnoreCase(role.getCode())) {
						return "redirect:/admin";
					}
				}
			}
			return "redirect:/errors";
		}
	}

	@RequestMapping(value = "/admin")
	public ModelAndView unknow(HttpServletRequest request,HttpServletResponse response) {
		String  userName = "";
		String  X_Token = "";
		Cookie[] cookies = request.getCookies(); 
		if(cookies!=null)    
		{      
		    for (int i = 0; i < cookies.length; i++)     
		    {    
		       Cookie c = cookies[i];         
		       if(c.getName().equalsIgnoreCase("userName")){    
		    	   userName = c.getValue();    
		       }else if(c.getName().equalsIgnoreCase("X-Token")){    
		        	X_Token = c.getValue();    
		       }          
		    }     
		 } 

		if(StringUtils.isEmpty(X_Token)){
			X_Token = request.getParameter(SECURITY_TOKEN_HEADER);
			if(StringUtils.isEmpty(X_Token) == false){
		        AccessTokenInfo accessTokenInfo = getAccessTokenInfo(X_Token);
		        if(StringUtils.isEmpty(accessTokenInfo) == false){
		            this.saveCookieby(response, X_Token, accessTokenInfo);
		        }
				if(StringUtils.isEmpty(userName)){
					userName = accessTokenInfo.getUserName();
				}
			}		
		}
		
		EmployeeResponse loginInfo = new EmployeeResponse();
		if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(X_Token)){
			loginInfo =viewService.getEmployeeByLoginId(userName, X_Token);		
		    if(null != loginInfo){
		    	loginInfo.setAccessToken(X_Token);
		    }
		}		

		return new ModelAndView("admin_index","loginInfo",loginInfo);
	}

	private AccessToken getAccessToken(String code) {

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		List<String> auth = new ArrayList<>();
		auth.add("Basic d2ViQXBwOndlYkFwcA==");
		headers.put("Authorization", auth);

		ResponseEntity<AccessToken> exchange = restTemplate.exchange(
				String.format(env.getRequiredProperty("token.endpoint"), code),
				HttpMethod.POST, new HttpEntity<>("", headers),
				AccessToken.class);
		return exchange.getBody();
		
	}

    private void saveCookieby(HttpServletResponse response,String  access_token,AccessTokenInfo accessTokenInfo){
        response.addCookie(new Cookie("X-Token", access_token));
        response.addCookie(new Cookie("userName", accessTokenInfo.getUserName()));
        response.addCookie(new Cookie("Authorities", accessTokenInfo
                .getAuthorities().toString()));
		if (null != accessTokenInfo.getExp()) {
			response.addCookie(new Cookie("Token-Exp", accessTokenInfo.getExp()
					.toString()));
		}
    }
	
	private AccessTokenInfo getAccessTokenInfo(String accessToken) {
		return restTemplate.getForObject(String.format(
				env.getProperty("checkToken.endpoint"), accessToken),
				AccessTokenInfo.class);

	}

}
