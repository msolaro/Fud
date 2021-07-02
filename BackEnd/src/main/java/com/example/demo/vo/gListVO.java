package com.example.demo.vo;

import java.util.List;

/**
 * 
 * @author msolaro
 *
 */
public class gListVO {

	private List<String> items;
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

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> list) {
		this.items = list;
	}

}
