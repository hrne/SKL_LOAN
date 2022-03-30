package com.st1.ifx.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// TODO: uncomment for spring security
//@EnableWebMvc
//@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		super.addViewControllers(registry);
		logger.info("addViewController --> /login/form to login");
		registry.addViewController("login/form").setViewName("login");
	}

}
