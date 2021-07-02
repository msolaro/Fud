package com.example.demo.item;

/**
 * 
 * @author msolaro
 * @author rlspick
 *
 */
public class Item {
	private String name = "";
	private int quantity = 0;
	private String expirationDate = "";
	private String category = "";
	private String sessionKey = "";

	public Item() {
		super();
	}

	public Item(String name) {
		this.name = name;
		this.quantity++;
	}

	/**
	 * Get Item name
	 * 
	 * @return Item Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set Item name
	 * 
	 * @param name Name of Item 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get Item quantity
	 * 
	 * @return Item Quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Set Item quantity
	 * 
	 * @param quantity Quantity of given Item
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Get Item expiration date
	 * 
	 * @return Expiration Date
	 */
	public String getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Set Item expiration date
	 * 
	 * @param expirationDate Expiration Date of given Item
	 */
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Set Item category
	 * 
	 * @param category Category that given Item belongs to
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Get Item category
	 * 
	 * @return Item Category
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * Get Item Session Key
	 * 
	 * @return Session Key
	 */
	public String getSessionKey() {
		return this.sessionKey;
	}
}
