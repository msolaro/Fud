package com.example.fud.PantryModel;

import java.util.ArrayList;

public class PantryList {
    /**
     * The ArrayList that holds the items in the pantry
     */
    private ArrayList<Pantry> mEntries = new ArrayList<>();
    /**
     * Total number of entries in the ArrayList
     */
    private int mTotalEntries = 0;

    /**
     * Method used to add a item to the pantry
     * @param entry The Pantry item being added
     */
    public void addItem(Pantry entry) {
        mEntries.add(entry);
        mTotalEntries++;
    }

    /**
     * Method used to remove an item from the App
     * @param name The String name of the item to be removed
     */
    public void removeItem(String name) {
        for (Pantry p: mEntries) {
            if (p.getName().equals(name)){
                mEntries.remove(p);
            }
        }
        mTotalEntries--;
    }

    /**
     * Method used to remove an item from the App
     * @param entry The String name of the item to be removed
     */
    public void removeItem(Pantry entry) {
        mEntries.remove(entry);
        mTotalEntries--;
    }

    /**
     * Return method to get all entries
     * @return Returns the ArrayList of items in the pantry
     */
    public ArrayList<Pantry> getAllEntries() {
        return mEntries;
    }

    /**
     * Return method to get the entry count
     * @return Returns the number of items in the panty
     */
    public int getEntryCount() {
        return mTotalEntries;
    }

}
