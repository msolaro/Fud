package com.example.demo.vo;

import java.util.List;

import com.example.demo.item.Item;

/**
 * 
 * @author msolaro
 *
 */
public class pantryVO {
	private List<Item> foods;
	private String message;
	private boolean status;

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Item> getFood() {
		return foods;
	}

	public void setItems(List<Item> list) {
		this.foods = list;
	}

}
