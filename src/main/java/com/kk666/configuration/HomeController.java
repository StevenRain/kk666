package com.kk666.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
public class HomeController {
	@RequestMapping(value = "/")
	public String index() {
		log.info("swagger-ui.html");
		return "redirect:swagger-ui.html";
	}
}
