package com.example.demo.pantry;

import javax.persistence.Id;

import com.example.demo.item.Item;

import java.util.ArrayList;
import java.util.List;

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
public class Pantry {

	@Column(name = "food_id")
	@ElementCollection(targetClass = Integer.class)
	private List<Number> food_id = new ArrayList<>();

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
	@ElementCollection(targetClass = String.class)
	private List<String> category = new ArrayList<>();

	@Column(name = "pantry")
	private String pantryName = "";

	@Column(name = "sessionKey")
	private String sessionKey = "";

	@Id
	@Column(name = "pantry_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pantryID;

	public Pantry(String pantryName) {
		this.pantryName = pantryName;
	}

	public Pantry() {

	}

	/**
	 * Set Pantry Session Key
	 * 
	 * @param sessionKey Session Key to set for Pantry
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * Get Pantry Session Key
	 * 
	 * @return Session Key
	 */
	public String getSessionKey() {
		return this.sessionKey;
	}

	/**
	 * Add Item to Pantry
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
	 * Get Item from Pantry
	 * 
	 * @param name Item to Get
	 * @return Item 
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
	 * Delete Item from Pantry
	 * 
	 * @param name Item to Delete
	 * @return Item
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
	 * Check if Item exists in Pantry
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
	 * Get all Items in Pantry
	 * 
	 * @return All Items in Pantry
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

}
