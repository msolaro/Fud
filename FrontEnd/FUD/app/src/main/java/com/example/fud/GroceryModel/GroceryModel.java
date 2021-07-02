package com.example.fud.GroceryModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fud.GroceryModel.GroceryList;

/**
 * This class represents a GroceryModel which is a LiveData container for the grocery list in the app.
 * @author Emily Kinne
 * @since 3-26-2020
 */
public class GroceryModel extends ViewModel {
    /**
     * MutableLiveData that holds all of the items in grocery list
     */
    public MutableLiveData<GroceryList> groceryList = new MutableLiveData<>();
    /**
     * Constructor of the groceryModel
     */
    public GroceryModel() {
        groceryList.setValue(new GroceryList());
    }

    /**
     * Method used to add a new item to the groceryModel
     * @param val The String title of new item
     */
    public void addItem(String val) {
        GroceryList g = groceryList.getValue();
        if(g != null) {
            g.addItem(val);
            groceryList.setValue(g);
        }
    }

    /**
     * Method used to remove an item from the groceryModel
     * @param val The String title of item to be removed
     */
    public void removeItem(String val) {
        GroceryList g = groceryList.getValue();
        if(g != null) {
            g.removeItem(val);
            groceryList.setValue(g);
        }
    }

    /**
     * Method used to get the size of groceryList
     */
    public int getItemCount() {
        GroceryList g = groceryList.getValue();
        if(g != null) {
           return g.getItemCount();
        }
        else{ return 0; }
    }
}
