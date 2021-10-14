package com.rmdaw.module15.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping
	public String getHome(Model model) {
		
		return "index";
	}
	
	
	@RequestMapping("/bug")
	public String getBugged(Model model) {
		Long var1 = 200L;
		model.addAttribute("var1", var1);
		return "index";
	}
}
