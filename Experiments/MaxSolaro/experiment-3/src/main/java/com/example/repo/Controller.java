package com.example.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	
	@Autowired
	PojoRepository p;
	
	@GetMapping("/")
	String sayHello() {		
		return "hello world";
	}
}
