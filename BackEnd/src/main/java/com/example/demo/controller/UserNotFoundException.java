package com.example.demo.controller;

/**
 * 
 * @author msolaro
 *
 */
class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 * Throws an exception when a user cannot be found (by ID)
	 * 
	 * @param id
	 */
	UserNotFoundException(Long id) {
		super("Could not find User " + id);
	}

	/**
	 * 
	 * Throws an exception when a user cannot be found (by Username)
	 * 
	 * @param username
	 */
	UserNotFoundException(String username) {
		super("Could not find User " + username);
	}
}
