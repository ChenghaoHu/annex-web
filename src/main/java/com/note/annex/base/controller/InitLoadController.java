package com.note.annex.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InitLoadController {

	@RequestMapping("/")
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/ftl/login.ftl");
		return mv;
	}
	
}
