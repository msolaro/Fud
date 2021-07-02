package com.example.fud.PantryModel;

import java.util.ArrayList;

/**
 * This class is for a pantry object
 */
public class Pantry {

    private String mType;
    private String mName;
    private Integer mQuantity;
    private String mExpiration;

    /**
     * An array list to store all of the different types (categories) of an item
     */
    private final static ArrayList<String> mTypesAmounts = new ArrayList<>();
    static{
        mTypesAmounts.add("meat");
        mTypesAmounts.add("fruit");
        mTypesAmounts.add("vegetable");
        mTypesAmounts.add("dairy");
        mTypesAmounts.add("other");
    }

    /**
     * The pantry item constructor that needs the items name, type, quantity, and expiration date.
     * @param name Item's name
     * @param type Item's type
     * @param quantity Item's quantity
     * @param expiration Item's expiration date
     */
    public Pantry(String name, String type, Integer quantity, String expiration) {
        mName = name;
        mType = type;
        mQuantity = quantity;
        mExpiration = expiration;
    }

    /**
     * method returns all of the different types (categories) of an item
     * @return a string array
     */
    public static String[] getEntryTypes() {
        ArrayList<String> types = new ArrayList<>(mTypesAmounts);
        return types.toArray(new String[]{""});
    }

    /**
     * Returns the name of the item.
     * @return mTotalEntries
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the item's name
     * @param name
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Returns the item's type
     * @return mType
     */
    public String getType() {
        return mType;
    }

    /**
     * Sets the item's type
     * @param type
     */
    public void setType(String type) {
        mType = type;
    }

    /**
     * Returns the item's quantity
     * @return mQuantity
     */
    public Integer getQuantity() {
        return mQuantity;
    }

    /**
     * Sets the item's quantity
     * @param quantity
     */
    public void setmQuantity(Integer quantity) {
        mQuantity = quantity;
    }

    /**
     * Returns the item's expiration
     * @return mExpiration
     */
    public String getExpiration() {
        return mExpiration;
    }

    /**
     * Sets the item's expiratation date
     * @param expiration
     */
    public void setmExpiration(String expiration) { mExpiration = expiration; }

}
