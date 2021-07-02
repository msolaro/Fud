package com.example.demo.households;

import javax.persistence.Id;

import com.example.demo.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math; 

import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
public class Households {

	@Id
	@Column(name = "household_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "username")
	private String username = null;

	@Column(name = "household")
	private String household = null;

	@Column(name = "sessionKey")
	private String sessionKey = "";

	@Column(name = "roomates")
	@ElementCollection(targetClass = Long.class)
	private List<Number> roomates = new ArrayList<>();

	@Column(name = "quantity")
	@ElementCollection(targetClass = Integer.class)
	private List<Number> quantity = new ArrayList<>();

	@Column(name = "expiration_date")
	@ElementCollection(targetClass = String.class)
	private List<String> expiration_date = new ArrayList<>();

	@Column(name = "food_name")
	@ElementCollection(targetClass = String.class)
	private List<String> food_name = new ArrayList<>();

	@Column(name = "category")

	@ElementCollection(targetClass=String.class)
	private List<String> category = new ArrayList<>();
	
	@Column(name = "pin")
	private String pin = "";
	
	public Households() {}
	

	public Households(String household, String username, String[] roomates) {
		this.username = username;
		this.household = household;

	}

	/**
	 * Get Household ID
	 * 
	 * @return Household ID
	 */
	public Long getID() {
		return id;
	}

	/**
	 * Get Username from Household
	 * 
	 * @return Household Username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get Household
	 * 
	 * @return Household
	 */
	public String getHousehold() {
		return household;
	}

	/**
	 * Add a User to the Household
	 * 
	 * @param roommateid ID of User to Add
	 */
	public void addRoomate(long roommateid) {
		roomates.add(roommateid);
	}

	public void removeRoomate(long roommateid) {
		roomates.remove(roommateid);
	}

	/**
	 * Set Household ID
	 * 
	 * @param id2 ID to set for Household
	 */
	public void setId(Long id2) {
		this.id = id2;

	}

	/**
	 * Set Username
	 * 
	 * @param newUsername New Username
	 * @return New Username
	 */
	public String setUsername(String newUsername) {
		username = newUsername;
		return username;
	}

	/**
	 * Reset (or Replace) Household
	 * 
	 * @param houseHold2 Household (to reset or replace) current Household
	 */
	public void setHousehold(String houseHold2) {
		this.household = houseHold2;
	}

	/**
	 * Set Household Session Key
	 * 
	 * @param sessionKey Session Key to set as Household Session Key
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * Get all Users in Household
	 * 
	 * @return Roomates in Household
	 */
	public List<Number> getRoomates() {
		return roomates;
	}

	/**
	 * Add Item to Household Pantry
	 * 
	 * @param food Item to Add
	 * @return True/False
	 */
	public boolean addFood(Item food) {

		if (food_name.contains(food.getName())) {

			int index = food_name.indexOf(food.getName());

			int q = (int) quantity.get(index) + food.getQuantity();
			quantity.set(index, q);

			String exp = food.getExpirationDate();
			expiration_date.set(index, exp);

			return false;

		} else {
			food_name.add(food.getName());
			quantity.add(food.getQuantity());
			expiration_date.add(food.getExpirationDate());
			category.add(food.getCategory());
		}
		return true;

	}

	/**
	 * Find Item in Household Pantry
	 * 
	 * @param name Item to Find
	 * @return Item in Pantry
	 */
	public Item getFood(String name) {
		Item ret = new Item();
		int index = food_name.indexOf(name);

		ret.setName(name);
		ret.setCategory(category.get(index));
		ret.setExpirationDate(expiration_date.get(index));
		ret.setQuantity((int) quantity.get(index));

		return ret;
	}

	/**
	 * Remove Item from Household Pantry
	 * 
	 * @param name Item to Delete
	 * @return Item in Pantry 
	 */
	public Item deleteFood(String name) {
		Item ret = new Item();
		if (food_name.contains(name)) {
			int index = food_name.indexOf(name);
			ret.setName(food_name.remove(index));
			ret.setQuantity((int) quantity.remove(index));
			ret.setExpirationDate(expiration_date.remove(index));
			ret.setCategory(category.remove(index));

		}

		return ret;
	}

	/**
	 * Check if Item exists in Household Pantry
	 * 
	 * @param in Item to Check
	 * @return True/False
	 */
	public boolean contains(Item in) {
		if (food_name.contains(in.getName()))
			return true;
		else
			return false;
	}

	/**
	 * Get all Items in Household Pantry
	 * 
	 * @return Items in Pantry
	 */
	public List<Item> getAll() {
		List<Item> ret = new ArrayList<Item>();
		int i = 0;
		while (i < food_name.size()) {
			Item current = new Item();
			current.setCategory(category.get(i));
			current.setExpirationDate(expiration_date.get(i));
			current.setName(food_name.get(i));
			current.setQuantity((int) quantity.get(i));
			ret.add(current);
			i++;
		}
		return ret;

	}
	
	public String newPin()
	{
		String ret = "";
		Random rand = new Random();
		for(int i=0;i<6;i++)
		{
		int num = rand.nextInt(80);
		ret = ret + (char)(num + 42);
		}
		this.pin = ret;
		return ret;
	}
	
	public String getPin()
	{
		return pin;
	}

}
