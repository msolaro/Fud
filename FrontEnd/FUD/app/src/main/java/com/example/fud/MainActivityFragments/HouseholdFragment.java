package com.example.fud.MainActivityFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.example.fud.PantryModel.Pantry;
import com.example.fud.PantryModel.PantryList;
import com.example.fud.PantryModel.PantryModel;
import com.example.fud.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static com.example.fud.AppActivities.MainActivity.HOUSEHOLD;
import static com.example.fud.AppActivities.MainActivity.SESH_KEY;

public class HouseholdFragment extends Fragment implements Observer<PantryList> {

    /**
     * Strings to store user inputted information when adding new items
     */
    private String mCategory, newItem, mExpiration;
    /**
     * Integer to store quantity of item
     */
    private Integer mQuantity;
    /**
     * Strings to store user's session key
     */
    private String session;
    /**
     * Strings to store user's household
     */
    private String household;
    /**
     * Edit texts for the item's name and quantity inputted by the user
     */
    private EditText item_txt, quantity_txt;
    /**
     * Table layouts for the different categories of food items
     */
    private TableLayout meatTable, fruitTable, vegetableTable, otherTable, dairyTable;
    /**
     * Textview for the item's expiration date inputted by the user
     */
    private TextView expiration_txt, house_name_txt;
    /**
     * String url for the shared pantry
     */
    private String SHARE_URL;
    /**
     * The view being inflated for this fragment located in the layout resources
     */
    private View v;
    /**
     * The context of this fragment
     */
    private Context mContext;
    /**
     * Shared Preferences used to automatically save the user's email and session key
     */
    private SharedPreferences mSharedPreferences;
    /**
     * A liveData container for the pantry list. It can be observed in order to update the UI
     */
    private PantryModel house;
    /**
     * ArrayList that keeps track of the items that are checked and need to be changed
     */
    private ArrayList<Pantry> itemSelected;
    /**
     * ArrayList that keeps track of the items that are checked and need to be changed (Stored as ints: indices)
     */
    private ArrayList<Integer> indicesSelected;
    /**
     * ArrayList is a copy of pantry and keeps track of the items
     */
    private ArrayList<Pantry> copy;
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_household, container, false);

        mContext = container.getContext();
        session = mSharedPreferences.getString(SESH_KEY,null);
        household = mSharedPreferences.getString(HOUSEHOLD,null);
        indicesSelected = new ArrayList<>();
        itemSelected = new ArrayList<>();

        SHARE_URL = getString(R.string.URL) + "hhpantry/";
        item_txt = v.findViewById(R.id.add_item);
        quantity_txt = v.findViewById(R.id.add_quantity);

        meatTable = v.findViewById(R.id.meat_table);
        fruitTable = v.findViewById(R.id.fruit_table);
        vegetableTable = v.findViewById(R.id.vegetable_table);
        dairyTable = v.findViewById(R.id.dairy_table);
        otherTable = v.findViewById(R.id.other_table);

        expiration_txt = (TextView) v.findViewById(R.id.add_exp_date);
        expiration_txt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { getDate(v); }
        });

        house_name_txt= v.findViewById(R.id.house_name);
        house_name_txt.setText(household);

        Spinner spinner = v.findViewById(R.id.new_entry_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, Pantry.getEntryTypes());
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mCategory = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

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

        house = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(PantryModel.class);
        house.pantryList.observe(this, this);

        copy = new ArrayList<Pantry>();

        mainAct = (MainActivity) getActivity();
        copy = mainAct.houseRequest;

        for (int i=0; i<copy.size(); i++){
            house.addItem(copy.get(i));
        }

        return v;
    }

    /**
     * popup of calendar to set the expiration of new item
     * @param view expiration date textview that was clicked
     */
    private void getDate (View view) {
        final Calendar c = Calendar.getInstance();

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        expiration_txt.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    /**
     * Adds a new item to the LiveData container list
     * @param view Add button that was clicked
     */
    public void onAddClicked (View view) {
        newItem = item_txt.getText().toString();
        mQuantity = Integer.parseInt(quantity_txt.getText().toString());
        mExpiration = expiration_txt.getText().toString();

        if (quantity_txt.getText().equals("")){  mQuantity = 1; }
        if (expiration_txt.getText().equals("")){
            mExpiration = new SimpleDateFormat("mm/dd/yyyy", Locale.getDefault()).format(new Date());
        }

        final Pantry entry = new Pantry(newItem, mCategory, mQuantity, mExpiration);

        JSONObject params = new JSONObject();
        try {
            params.put("name", newItem);
            params.put("quantity", mQuantity);
            params.put("expirationDate", mExpiration);
            params.put("category", mCategory);
            params.put("sessionKey", session);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Put request
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, SHARE_URL + "add", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Logging", response.toString());
                try {
                    if (response.getBoolean("status") == true) { //if post works
                        Log.d("Logging", "SUCCESS");
                        house.addItem(entry);
                        mainAct.houseRequest = copy;

                    } else {
                        Log.d("Logging", "FAILURE: food not added");
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
        quantity_txt.setText("");
        expiration_txt.setText("");

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
            final Pantry item = itemSelected.get(i);

            JSONObject params = new JSONObject();
            try {
                params.put("name", item.getName());
                params.put("sessionKey", session);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Post Request
            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, SHARE_URL + "delete", params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Logging", response.toString());
                    try {
                        if (response.getBoolean("status") == true) { //if post works
                            Log.d("Logging", "SUCCESS");
                            house.removeItem(item);
                            mainAct.houseRequest = copy;

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

        }
        itemSelected.removeAll(itemSelected);
        itemSelected = new ArrayList<>();
    }


    /**
     * This method handles whenever the pantry in the LiveData container is changed. It will dynamically create the table layout with
     * the items and checkboxes so if one/multiple lists are selected they can be deleted. Also gives the ability to add items.
     * @param list The PantryList variable we are observing.
     */
    @Override
    public void onChanged(PantryList list) {

        meatTable.removeAllViews();
        fruitTable.removeAllViews();
        vegetableTable.removeAllViews();
        dairyTable.removeAllViews();
        otherTable.removeAllViews();
        int index = 0;
        copy = new ArrayList<>();

        for (Pantry item : list.getAllEntries()) {
            TableRow row = new TableRow(mContext);
            TextView quantity = new TextView(mContext);
            TextView name = new TextView(mContext);
            TextView expiration = new TextView(mContext);

            quantity.setText(item.getQuantity().toString());
            quantity.setTextColor(getResources().getColor(R.color.medium_text));
            quantity.setTextSize(22);
            quantity.setPadding(50, 0, 0, 0);

            name.setText(item.getName());
            name.setTextColor(getResources().getColor(R.color.medium_text));
            name.setBackgroundColor(getResources().getColor(R.color.background));
            name.setTextSize(25);
            name.setWidth(650);
            name.setPadding(50, 0, 0, 0);

            expiration.setText(item.getExpiration());
            expiration.setTextColor(getResources().getColor(R.color.medium_text));
            expiration.setBackgroundColor(getResources().getColor(R.color.background));
            expiration.setTextSize(20);

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
                        itemSelected.add(copy.get(view.getId())); //pantry item
                    }
                    else{
                        if(itemSelected.size()>0) {
                            Pantry rm = copy.get(view.getId());
                            int index = itemSelected.indexOf(rm);
                            itemSelected.remove(rm); //pantry item
                            indicesSelected.remove(index); //indices

                        }
                    }
                }
            });

            View spacerColumn = new View(mContext);

            row.addView(box);
            row.addView(quantity);
            row.addView(name);
            row.addView(expiration);
            row.setBackgroundResource(R.drawable.white_border);
            row.addView(spacerColumn, new TableRow.LayoutParams(1, 100));

            switch (item.getType()) {
                case "fruit":
                    fruitTable.addView(row);
                    break;
                case "meat":
                    meatTable.addView(row);
                    break;
                case "vegetable":
                    vegetableTable.addView(row);
                    break;
                case "dairy":
                    dairyTable.addView(row);
                    break;
                case "other":
                    otherTable.addView(row);
                    break;
                default:
                    break;
            }
            index++;
        }
        for (Pantry item : list.getAllEntries()) {
            copy.add(item);
        }
        Collections.sort(indicesSelected);
    }
}
