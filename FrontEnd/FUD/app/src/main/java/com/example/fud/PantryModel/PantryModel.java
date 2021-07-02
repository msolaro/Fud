package com.example.fud.PantryModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PantryModel extends ViewModel {
    /**
     * MutableLiveData that holds all of the items in pantry
     */
    public MutableLiveData<PantryList> pantryList = new MutableLiveData<>();
    /**
     * Constructor of the pantryModel
     */
    public PantryModel() {
        pantryList.setValue(new PantryList());
    }

    /**
     * Method used to add a new item to the pantryModel
     * @param entry The pantry item being added
     */
    public void addItem(Pantry entry) {
        PantryList p = pantryList.getValue();
        if(p != null) {
            p.addItem(entry);
            pantryList.setValue(p);
        }
    }

    /**
     * Method used to remove an item from the pantryModel
     * @param entry The String title of item to be removed
     */
    public void removeItem(String entry) {
        PantryList p = pantryList.getValue();
        if(p != null) {
            p.removeItem(entry);
            pantryList.setValue(p);
        }
    }

    /**
     * Method used to remove an item from the pantryModel
     * @param entry The Pantry object to be removed
     */
    public void removeItem(Pantry entry) {
        PantryList p = pantryList.getValue();
        if(p != null) {
            p.removeItem(entry);
            pantryList.setValue(p);
        }
    }

    /**
     * Method used to get the size of pantryList
     */
    public int getItemCount() {
        PantryList p = pantryList.getValue();
        if(p != null) {
            return p.getEntryCount();
        }
        else{ return 0; }
    }
}
