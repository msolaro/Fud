package com.example.fud.GroceryModel;

import java.util.ArrayList;

/**
 * This class is used to create a grocery list. Can add and remove items and return the total list and item count.
 * @author Emily Kinne
 * @since 3-26-2020
 */
public class GroceryList {
    /**
     * The ArrayList that holds the items in the grocery list
     */
    private java.util.ArrayList<String> mItems = new ArrayList<>();
    /**
     * Total number of entries in the ArrayList
     */
    private int mTotalItems = 0;

    /**
     * Method used to add a new item to the grocery list
     * @param entry The String name of new item
     */
    public void addItem(String entry) {
        mItems.add(entry);
        mTotalItems++;
    }

    /**
     *
     * @param i
     * @return
     */
    public String getItem(Integer i) {
        return mItems.get(i);
    }

    /**
     * Method used to remove an item from the grocery list
     * @param entry The String name of the item to be removed
     */
    public void removeItem(String entry) {
        mItems.remove(entry);
        mTotalItems--;
    }

    /**
     * Return method to get all items
     * @return Returns the ArrayList of items in the grocery list
     */
    public ArrayList<String> getAllItems() {
        return mItems;
    }

    /**
     * Return method to get the item count
     * @return Returns the total number of items in the grocery list
     */
    public int getItemCount() {
        return mTotalItems;
    }
}
