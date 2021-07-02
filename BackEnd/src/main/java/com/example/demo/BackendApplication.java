package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author msolaro
 * @author rlspick
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = { "websocket", "com.example.demo" })
public class BackendApplication {

	/**
	 * Main application for Spring Boot Project
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
