package com.example.demo.users;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * 
 * @author msolaro
 * @author rlspick
 *
 */
@Entity
public class Users {

	@Id
	@Column(name = "user_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "username")
	private String username = null;

	@Column(name = "password")
	private String password = null;

	@Column(name = "first_name")
	private String firstName = null;

	@Column(name = "last_name")
	private String lastName = null;

	@Column(name = "household")
	private String household = null;

	@Column(name = "sessionKey")
	private String sessionKey = null;

	public Users() {
	}

	public Users(String username, String pass, String first, String last) {
		this.username = username;
		this.password = pass;
		this.firstName = first;
		this.lastName = last;
		this.household = "unassaigned";
	}

	/**
	 * Get User ID
	 * 
	 * @return User ID
	 */
	public Long getID() {
		return id;
	}

	/**
	 * Get User username
	 * 
	 * @return User Username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get User password
	 * 
	 * @return User Password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Get User first name
	 * 
	 * @return User First Name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Get User last name
	 * 
	 * @return User Last Name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Get User household
	 * 
	 * @return Household of User
	 */
	public String getHousehold() {
		return household;
	}

	/**
	 * Get Session Key for User
	 * 
	 * @return Session Key
	 */
	public String getSessionKey() {
		return sessionKey;
	}

	/**
	 * Set User username
	 * 
	 * @param newUsername New Username for User
	 * @return New Username for User
	 */
	public String setUsername(String newUsername) {
		username = newUsername;
		return username;
	}

	/**
	 * Set User password
	 * 
	 * @param newPass New Password for User
	 * @return new Password for User
	 */
	public String setPassword(String newPass) {
		password = newPass;
		return password;
	}

	/**
	 * Set User first name
	 * 
	 * @param newFirstName New First Name for User
	 * @return New First Name for User
	 */
	public String setFirstName(String newFirstName) {
		firstName = newFirstName;
		return firstName;
	}

	/**
	 * Set User last name
	 * 
	 * @param newLastName New Last Name for User
	 * @return New Last Name for User
	 */
	public String setLastName(String newLastName) {
		lastName = newLastName;
		return lastName;
	}

	/**
	 * Reset (or Replace) User household
	 * 
	 * @param houseHold2 Household (to reset or replace) current User
	 */
	public void setHousehold(String houseHold2) {
		this.household = houseHold2;
	}

	/**
	 * Set User Session Key
	 * 
	 * @param sessionkey2 Session Key to set as current Session Key for User
	 */
	public void setSessionKey(String sessionkey2) {
		this.sessionKey = sessionkey2;
	}

	/**
	 * Set User ID
	 * 
	 * @param id2 Set ID for current User
	 */
	public void setId(Long id2) {
		this.id = id2;
	}

}
