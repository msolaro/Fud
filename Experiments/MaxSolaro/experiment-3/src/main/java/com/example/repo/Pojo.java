package com.example.repo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;

@Entity
public class Pojo {
	
	@Column(name = "name")
	String name;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	String getName() {
		return this.name;
	}
	
	Long getId() {
		return this.id;
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	void setId(long id) {
		this.id = id;
	}
}
