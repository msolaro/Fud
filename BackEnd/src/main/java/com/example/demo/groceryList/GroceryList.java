package com.example.demo.groceryList;

import javax.persistence.*;

import java.util.HashSet;
import java.util.List;

import javax.persistence.CollectionTable;
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
public class GroceryList {

	@Id
	@Column(name = "groceryList_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long groceryList_id;
	@Column(name = "sessionKey")
	private String sessionKey = "";
	@Column(name = "food")
	private String food = "";
	@ElementCollection
	@CollectionTable(name = "gList_items")
	private List<String> gList_items;

	@Column(name = "groceryList")
	private String groceryListName = "";

	public GroceryList() {
	}

	/**
	 * Get Grocery List name
	 * 
	 * @return Grocery List Name
	 */
	public String getGroceryListName() {
		return groceryListName;
	}

	/**
	 * Set Grocery List name
	 * 
	 * @param groceryListName Name of the Grocery List
	 */
	public void setGroceryListName(String groceryListName) {
		this.groceryListName = groceryListName;
	}

	/**
	 * Get Grocery List ID
	 * 
	 * @return Grocery List ID
	 */
	public long getGroceryList_id() {
		return groceryList_id;
	}

	/**
	 * Set Grocery List ID
	 * 
	 * @param groceryList_id ID of the Grocery List
	 */
	public void setGroceryList_id(long groceryList_id) {
		this.groceryList_id = groceryList_id;
	}

	/**
	 * Get item(s) from Grocery List
	 * 
	 * @return Food Item from Grocery List
	 */
	public String getFood() {
		return food;
	}

	/**
	 * Set chosen Item
	 * 
	 * @param food Chosen Food Item
	 */
	public void setFood(String food) {
		this.food = food;
	}

	/**
	 * Set Session Key
	 * 
	 * @param sessionKey Session Key to set as the new Session Key
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * Get Session Key
	 * 
	 * @return Grocery List Session Key
	 */
	public String getSessionKey() {
		return this.sessionKey;
	}

	/**
	 * Get all Items in Pantry
	 * 
	 * @return Items in Pantry
	 */
	public HashSet<String> getAll() {
		HashSet<String> pantryItems = new HashSet<>();

		pantryItems.addAll(gList_items);

		return pantryItems;
	}

	/**
	 * Add Item
	 * 
	 * @param item Item to add to Grocery List
	 */
	public void addItem(String item) {
		gList_items.add(item);

	}

	/**
	 * Return Grocery List
	 * 
	 * @return Grocery List
	 */
	public List<String> getgList_items() {
		return gList_items;
	}

	/**
	 * Reset (or Replace) Grocery List with given List
	 * 
	 * @param gList_items The Grocery List of the Household
	 */
	public void setgList_items(List<String> gList_items) {
		this.gList_items = gList_items;
	}

	/**
	 * Remove Item from Grocery List (if exists)
	 * 
	 * @param food2 Food Item to remove
	 * @return True/False
	 */
	public boolean removeItem(String food2) {
		if (this.gList_items.contains(food2)) {
			this.gList_items.remove(food2);
			return true;
		}
		return false;
	}

}
