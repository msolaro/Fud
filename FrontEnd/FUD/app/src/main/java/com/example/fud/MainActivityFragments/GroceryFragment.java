package com.example.fud.MainActivityFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fud.AppActivities.MainActivity;
import com.example.fud.Controller.AppController;
import com.example.fud.GroceryModel.GroceryList;
import com.example.fud.GroceryModel.GroceryModel;
import com.example.fud.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.fud.AppActivities.MainActivity.SESH_KEY;


public class GroceryFragment extends Fragment implements Observer<GroceryList> {

    /**
     * Url for adding and deleting items in grocery list
     */
    String GROC_URL;
    /**
     * A liveData container for the grocery list. It can be observed in order to update the UI
     */
    private GroceryModel grocery;
    /**
     * Element for entering the name of a new list
     */
    private EditText item_txt;
    /**
     * ArrayList that keeps track of the items that are checked and need to be changed (Stored as strings)
     */
    private ArrayList<String> itemSelected;
    /**
     * ArrayList that keeps track of the items that are checked and need to be changed (Stored as ints: indices)
     */
    private ArrayList<Integer> indicesSelected;
    /**
     * ArrayList that holds a copy of the LiveData groceryModel from the server.
     */
    private ArrayList<String> copy;
    /**
     * The view being inflated for this fragment located in the layout resources
     */
    private View v;
    /**
     * The context of this fragment
     */
    private Context mContext;
    /**
     * String to hold the session key
     */
    private String session;
    /**
     * Int to hold the number of items in the grocery list
     */
    private TextView numItems;
    /**
     * Shared Preferences used to save the user's session key
     */
    private SharedPreferences mSharedPreferences;
    /**
     * The fragment's parent Activity
     */
    public MainActivity mainAct;

    /**
     * This method loads shared preferences for the app
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSharedPreferences = context.getSharedPreferences(getString(R.string.package_name), Context.MODE_PRIVATE);
    }

    /**
     * This method creates the view for the grocery model and requests the initial grocery list from the server.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_grocery, container, false);

        GROC_URL = getString(R.string.URL) + "groceryList/";

        mContext = container.getContext();
        session = mSharedPreferences.getString(SESH_KEY,null);

        numItems = (TextView) v.findViewById(R.id.items);
        indicesSelected = new ArrayList<>();
        itemSelected = new ArrayList<>();

        item_txt = v.findViewById(R.id.add_item);
        ImageButton addButton = (ImageButton) v.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!item_txt.getText().toString().equals("")){ onAddClicked(v); }
            }
        });

        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDeleteClicked(v);
            }
        });

        grocery = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(GroceryModel.class);
        grocery.groceryList.observe(this, this);

        copy = new ArrayList<>();

        mainAct = (MainActivity) getActivity();
        copy = mainAct.grocRequest;

        for (int i=0; i<copy.size(); i++){
            grocery.addItem(copy.get(i));
        }

        numItems.setText(copy.size() + " items");
        return v;
    }

    /**
     * Adds a new item to the LiveData container list
     * @param view Add button that was clicked
     */
    public void onAddClicked (View view) {
        final String newItem = item_txt.getText().toString();

        JSONObject params = new JSONObject();
        try {
            params.put("food", newItem);
            params.put("sessionKey", session);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Put request
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.PUT, GROC_URL + "addItem", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Logging", response.toString());
                try {
                    if (response.getBoolean("status") == true) { //if post works
                        Log.d("Logging", "SUCCESS");
                        grocery.addItem(newItem);
                        mainAct.grocRequest = copy;
                    } else {
                        Log.d("Logging", "FAILURE");
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PDialog", "request encountered an error");
                Log.d("Volley", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        AppController.getInstance(mContext).addToRequestQueue(jsonObject);
        item_txt.setText("");
    }

    /**
     * Deletes ALL of the checked lists from the LiveData container.
     * @param view Delete button that was clicked
     */
    public void onDeleteClicked (View view) {
        int loop = itemSelected.size();
        for (int i=0; i<loop; i++){
            copy.remove(indicesSelected.get(i));
        }
        indicesSelected.removeAll(indicesSelected);

        for (int i=0; i<loop; i++){
            final String item = itemSelected.get(i);

            JSONObject params = new JSONObject();
            try {
                params.put("food", item);
                params.put("sessionKey", session);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Post Request
            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, GROC_URL + "deleteItem", params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Logging", response.toString());
                    try {
                        if (response.getBoolean("status") == true) { //if post works
                            Log.d("Logging", "SUCCESS");
                            grocery.removeItem(item);
                            mainAct.grocRequest = copy;
                        } else {
                            Log.d("Logging", "FAILURE");
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("PDialog", "request encountered an error");
                    Log.d("Volley", error.toString());
                }
            });
            // Add the request to the RequestQueue.
            AppController.getInstance(mContext).addToRequestQueue(jsonObject);
            numItems.setText(copy.size() + " items");
        }
        itemSelected.removeAll(itemSelected);
        itemSelected = new ArrayList<>();
    }

    /**
     * This method handles whenever the grocery list in the LiveData container is changed. It will dynamically create the table layout with
     * the items and checkboxes so if one/multiple lists are selected they can be deleted. Also gives the ability to add items.
     * @param groc The GroceryList variable we are observing.
     */
    @Override
    public void onChanged(GroceryList groc) {
        TableLayout table = v.findViewById(R.id.table);
        table.removeAllViews();
        int index = 0;
        copy = new ArrayList<>();

        for (String item : groc.getAllItems()) {
            copy.add(item);
        }

        for (String item : groc.getAllItems()) {
            TableRow row = new TableRow(mContext);
            TextView name = new TextView(mContext);

            name.setText(item);
            name.setTextColor(getResources().getColor(R.color.medium_text));
            name.setBackgroundColor(getResources().getColor(R.color.background));
            name.setTextSize(25);
            name.setPadding(50, 0, 0, 0);

            CheckBox box = new CheckBox(mContext);
            if(indicesSelected.size()>0 && indicesSelected.contains(index)){
                box.setChecked(true);
            }
            else{
                box.setChecked(false);
            }
            box.setId(index);
            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox cb = (CheckBox) view;
                    if (cb.isChecked()){
                        indicesSelected.add(view.getId()); //indices
                        itemSelected.add(copy.get(view.getId())); //names
                        numItems.setText(itemSelected.size() + "/" + grocery.getItemCount() + " items");
                    }
                    else {
                        if(indicesSelected.size()>0 && itemSelected.size()>0) {
                            String name = copy.get(view.getId());
                            int index = itemSelected.indexOf(name);
                            itemSelected.remove(name); //names
                            indicesSelected.remove(index); //indices
                        }
                        if (indicesSelected.size()>0 || itemSelected.size()>0) {
                            numItems.setText(indicesSelected.size() + "/" + grocery.getItemCount() + " items");
                        }
                        else if (indicesSelected.size()==0 || itemSelected.size()==0) {
                            numItems.setText(grocery.getItemCount() + " items");
                        }
                    }
                }
            });

            View spacerColumn = new View(mContext);

            row.addView(box);
            row.addView(name);
            row.setBackgroundResource(R.drawable.white_border);
            row.addView(spacerColumn, new TableRow.LayoutParams(1, 100));
            table.addView(row);
            index++;
        }
        if (indicesSelected.size()>0 || itemSelected.size()>0) {
            numItems.setText(indicesSelected.size() + "/" + grocery.getItemCount() + " items");
        }
        else if (indicesSelected.size()==0 || itemSelected.size()==0) {
            numItems.setText(grocery.getItemCount() + " items");
        }
        Collections.sort(indicesSelected);
    }
}

