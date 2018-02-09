package com.demo.service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
@RefreshScope(proxyMode=ScopedProxyMode.DEFAULT)
public class DemoController {

	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.url}")
	private String url;
	
	@GetMapping(value = "/username/{id}")
	@HystrixCommand(fallbackMethod="defaultUsername",
	commandProperties= {
			@HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value="5"),
			@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="6000")
	})
	public String username(@PathVariable Long id) throws Exception {
		if(id < 2L)
			throw new Exception("AAAAAAAAA");
		return username +"id--> "+id;
	}
	
	private String defaultUsername(Long id) {
		return "Error with id: "+id;
	}
	@GetMapping(value = "/url")
	public String url() {
		return url;
	}
}
