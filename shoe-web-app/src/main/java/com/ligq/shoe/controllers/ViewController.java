package com.ligq.shoe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
	
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
}
