package com.ligq.shoe.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ligq.shoe.model.AccessToken;
import com.ligq.shoe.model.AccessTokenInfo;


@Controller
public class ViewController {
	
	@Autowired
	private Environment env;

	private RestTemplate restTemplate = new RestTemplate();

	
	@RequestMapping(value= "/")
	public String empty(){
		return "shoe_index";
	}	
	

	@RequestMapping(value= "/index")
	public String index(){
		return "shoe_index";
	}

	@RequestMapping(value= "/admin")
	public String admin(){
		return "admin_index";
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
		
		if (accessTokenInfo.getAuthorities().contains("ADMIN")) {
			return "redirect:/admin";
		} else if (accessTokenInfo.getAuthorities().contains("CALL_CENTER")) {
			return "redirect:/call";
		} else if (accessTokenInfo.getAuthorities().contains("CONTROL_CENTER")) {
			 return "redirect:/command";
		} else {
			return "redirect:/errors";
		}
	}

	@RequestMapping(value = "/")
	public String unknow(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String role = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase("Authorities"))
					role = cookie.getValue();
			}
		}
		if (role.contains("ADMIN")) {
			return "redirect:/admin";
		} else if (role.contains("CALL_CENTER")) {
			return "redirect:/call";
		}else if (role.contains("CONTROL_CENTER")) { 
			return "redirect:/command";
		}else {
			return "redirect:/errors";
		}
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

	private AccessTokenInfo getAccessTokenInfo(String accessToken) {
		return restTemplate.getForObject(String.format(
				env.getProperty("checkToken.endpoint"), accessToken),
				AccessTokenInfo.class);

	}

}
