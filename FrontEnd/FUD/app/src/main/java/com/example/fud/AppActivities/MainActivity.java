package com.example.fud.AppActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fud.Controller.AppController;
import com.example.fud.Controller.DisplayController;
import com.example.fud.MainActivityFragments.GroceryFragment;
import com.example.fud.MainActivityFragments.ChatFragment;
import com.example.fud.MainActivityFragments.HomeFragment;
import com.example.fud.MainActivityFragments.HouseholdFragment;
import com.example.fud.MainActivityFragments.RecipeFragment;
import com.example.fud.MainActivityFragments.PantryFragment;
import com.example.fud.PantryModel.Pantry;
import com.example.fud.R;

import com.google.android.material.navigation.NavigationView;

import android.os.Bundle;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class is the main activity of the app. It requests all of the user's data from the server
 * @author Emily Kinne
 * @since 4-26-2020
 *
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * App bar configuration variable
     */
    private AppBarConfiguration mAppBarConfiguration;
    /**
     * drawer layout variable
     */
    private DrawerLayout drawer;
    /**
     * Strings to store the user's email and session key loaded from shared preferences
     */
    private String session, username;
    /**
     * Shared Preferences used to automatically save the user's session key
     */
    private SharedPreferences mSharedPreferences;
    /**
     * Urls for getting data from backend
     */
    private String URL, GROC_URL, PANT_URL, HOUSE_URL;
    /**
     * String of the user's first name
     */
    public static String FIRSTNAME = "firstname";
    /**
     * String of the user's household
     */
    public static String HOUSEHOLD = "household";
    /**
     * String of the user's email that is passed from the login page.
     */
    public static String USR_KEY = "user key";
    /**
     * String of the user's session key that is passed from the login page.
     */
    public static String SESH_KEY = "session key";
    /**
     * Array to store grocery data from server
     */
    public ArrayList<String> grocRequest;
    /**
     * Array to store pantry data from server
     */
    public ArrayList<Pantry> pantRequest;
    /**
     * Array to store the shared pantry data from server
     */
    public ArrayList<Pantry> houseRequest;
    /**
     * Variable for JSONObject
     */
    private JSONObject jObj;
    /**
     * Variable for JSONArray
     */
    private JSONArray jArr;


    /**
     * This method is responsible for managing the main activity and switching between fragments
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DisplayController.getInstance(getApplicationContext()).nightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(getString(R.string.package_name), Context.MODE_PRIVATE);
        URL = getString(R.string.URL) + "getInfo";
        GROC_URL = getString(R.string.URL) + "groceryList/getItems";
        PANT_URL = getString(R.string.URL) + "pantry/get";
        HOUSE_URL = getString(R.string.URL) + "hhpantry/get";


        grocRequest = new ArrayList<>();
        pantRequest = new ArrayList<>();
        houseRequest = new ArrayList<>();

        Intent intent = getIntent();
        session = intent.getStringExtra(LoginActivity.SESH_KEY);
        username = mSharedPreferences.getString(USR_KEY,null);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        Log.d("Logging", session);
        getUser();
        getGrocery(session);
        getPantry(session);
        getHouse(session);
    }

    /**
     * Resume method to update the display mode
     */
    @Override
    protected void onResume(){
        if (DisplayController.getInstance(getApplicationContext()).changedMode()) {
            DisplayController.getInstance(getApplicationContext()).setChangedMode(false);
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }
        super.onResume();
    }

    /**
     * This method creates the toolbar layout
     * @param menu
     * @return super.onCreateOptionsMenu(menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is responsible for opening the settings activity when the settings button is clicked
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(SESH_KEY, session);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * This method manages switching between fragments
     * @param menuItem
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_pantry:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PantryFragment()).commit();
                break;
            case R.id.nav_grocery:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GroceryFragment()).commit();
                break;
            case R.id.nav_recipe:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeFragment()).commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
                break;
            case R.id.nav_household:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HouseholdFragment()).commit();
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This method is responsible for supporting the navigation functionality. It will load the navigation menu with all the pages
     * when the navigate button is clicked
     * @return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    /**
     * This function uses Volley requests to obtain the user's information from the server.
     */
    private void getUser() {

        JSONObject params = new JSONObject();
        try {
            params.put("sessionKey", session);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Post request
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Logging", response.toString());
                try {
                    final String firstname = response.getString("firstName");
                    final String household = response.getString("household");

                    mSharedPreferences.edit().putString(FIRSTNAME, firstname).commit();
                    mSharedPreferences.edit().putString(HOUSEHOLD, household).commit();

                    Log.d("Logging", "SUCCESS");

                } catch (JSONException e) {
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
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObject);
    }

    /**
     * Gets a user's grocery when they first login
     */
    private void getGrocery(String session){

        JSONObject params = new JSONObject();
        try {
            params.put("sessionKey", session);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Post request
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, GROC_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Logging", response.toString());
                try {
                    if (response.getBoolean("status") == true) { //if post works
                        jObj = response;
                        JSONArray arr  = jObj.getJSONArray("items");
                        for(int i=0;i<arr.length();i++) {
                            String item = arr.getString(i);
                            if (!item.equals("")) {
                                grocRequest.add(item);
                            }
                        }
                    } else {
                        Log.d("Logging", "FAILURE: could not get requested grocery");
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
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObject);
    }

    /**
     * Gets a user's pantry when they first login
     */
    private void getPantry(String session){

        JSONObject params = new JSONObject();
        try {
            params.put("sessionKey", session);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Post request
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, PANT_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Logging", response.toString());
                try {
                    if (response.getBoolean("status") == true) { //if post works
                        jObj = response;
                        jArr = jObj.getJSONArray("food");
                        parseArray("pantry");
                        Log.d("Logging", "SUCCESS");
                    } else {
                        Log.d("Logging", "FAILURE: could not get requested pantry");
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
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObject);
    }

    /**
     * Gets a user's shared grocery list with their household when user first logins
     */
    private void getHouse(String session){

        JSONObject params = new JSONObject();
        try {
            params.put("sessionKey", session);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Post request
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, HOUSE_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Logging", response.toString());
                try {
                    if (response.getBoolean("status") == true) { //if post works
                        jObj = response;
                        jArr = jObj.getJSONArray("food");
                        parseArray("house");
                        Log.d("Logging", "SUCCESS");
                    } else {
                        Log.d("Logging", "FAILURE: could not get requested shared pantry");
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
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObject);
    }


    private void parseArray(String request){

        try {
            for (int i = 0; i < jArr.length(); i++) {
                String n = jArr.getJSONObject(i).getString("name");
                Integer q = jArr.getJSONObject(i).getInt("quantity");
                String e = jArr.getJSONObject(i).getString("expirationDate");
                String c = jArr.getJSONObject(i).getString("category");

                if (n.equals("") || q.equals("") || e.equals("") || c.equals("")) {
                } else {
                    Pantry entry = new Pantry(n, c, q, e);
                    if (request.equals("pantry")) {
                        pantRequest.add(entry);
                    }
                    else {
                        houseRequest.add(entry);
                    }
                }
            }
        }catch (JSONException e){ }
    }
}
