package com.example.fud.AppActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fud.Controller.AppController;
import com.example.fud.Controller.DisplayController;
import com.example.fud.R;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.fud.AppActivities.MainActivity.FIRSTNAME;
import static com.example.fud.AppActivities.MainActivity.HOUSEHOLD;
import static com.example.fud.AppActivities.MainActivity.SESH_KEY;
import static com.example.fud.AppActivities.MainActivity.USR_KEY;

/**
 * This class is for the user settings to be able to change password, join/leave/create households, and some other app settings.
 * @author Emily Kinne
 * @since 4-28-2020
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * String to save user's data
     */
    private String household, newPass, confirmNewPass, session, username;
    /**
     * Urls for changing user's password and adding a household
     */
    String CHG_URL, JOIN_URL, LEAVE_URL, CREATE_URL, PIN_URL;
    /**
     * Shared Preferences used to automatically save the user's session key
     */
    SharedPreferences mSharedPreferences;

    /**
     * Creates the settings view. Creates all of the URLs for volley requests and loads all of the buttons and switches.
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
        setContentView(R.layout.activity_settings);

        CHG_URL = getString(R.string.URL) + "users/passwordChange";
        CREATE_URL = getString(R.string.URL) + "users/newHousehold";
        JOIN_URL = getString(R.string.URL) + "users/joinHousehold";
        LEAVE_URL = getString(R.string.URL) + "users/leaveHousehold";
        PIN_URL = getString(R.string.URL) + "hh/generatepin";

        mSharedPreferences = getSharedPreferences(getString(R.string.package_name), Context.MODE_PRIVATE);

        Intent intent = getIntent();
        session = intent.getStringExtra(LoginActivity.SESH_KEY);
        username = mSharedPreferences.getString(USR_KEY,null);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backarrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Switch darkMode = (Switch) findViewById(R.id.darkSwitch);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            darkMode.setChecked(true);
        else{
            darkMode.setChecked(false);
        }

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DisplayController.getInstance(getApplicationContext()).setIsNightModeEnabled(true);
                    DisplayController.getInstance(getApplicationContext()).setChangedMode(true);

                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
                else {
                    DisplayController.getInstance(getApplicationContext()).setIsNightModeEnabled(false);
                    DisplayController.getInstance(getApplicationContext()).setChangedMode(true);

                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Logs out of the app and opens the login activity
     * @param view Logout button that was clicked
     */
    public void onLogoutClicked(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mSharedPreferences.edit().remove(SESH_KEY).commit();
        mSharedPreferences.edit().remove(USR_KEY).commit();
        mSharedPreferences.edit().remove(FIRSTNAME).commit();
        mSharedPreferences.edit().remove(HOUSEHOLD).commit();
        mSharedPreferences.edit().clear().commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Allows a user to change their current password
     * @param view Change Password textview that was clicked
     */
    public void onChgPassClicked(View view) {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.change_password, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final ProgressDialog pdialog = new ProgressDialog(this);

        Button cancelButton = (Button) dialogView.findViewById(R.id.cancel_button);
        Button changeButton = (Button) dialogView.findViewById(R.id.change_button);
        final EditText newPass_txt = (EditText) dialogView.findViewById(R.id.new_pass_text);
        final EditText confirmNewPass_txt = (EditText) dialogView.findViewById(R.id.confirm_new_pass_text);
        final TextView errorMsg = (TextView) dialogView.findViewById(R.id.error_text);

        errorMsg.setVisibility(View.INVISIBLE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                pdialog.hide();
            }
        });
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPass = newPass_txt.getText().toString();
                confirmNewPass = confirmNewPass_txt.getText().toString();

                /**
                 * Passing new password
                 */
                JSONObject params = new JSONObject();
                try {
                    params.put("sessionKey", session);
                    params.put("password", newPass);
                    params.put("username", username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (newPass.equals("") || confirmNewPass.equals("")) {
                    errorMsg.setText(getString(R.string.error_field));
                    errorMsg.setVisibility(View.VISIBLE);
                } else if (!newPass.equals(confirmNewPass)) {
                    errorMsg.setText(getString(R.string.error_match));
                    errorMsg.setVisibility(View.VISIBLE);
                } else {
                    pdialog.setMessage("Loading...");
                    pdialog.show();
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, "http://10.24.226.237:8080/users/passwordChange", params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status") == true) { //if post works
                                    pdialog.hide();
                                    alertDialog.cancel();
                                    Toast toast = Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Log.d("Logging", "SUCCESS");
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
                            pdialog.hide();
                            Log.d("PDialog", "request encountered an error");
                            VolleyLog.d("Settings Page", "Error: " + error.getMessage());
                            Toast toast = Toast.makeText(getApplicationContext(), "Password was not changed", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                    AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "change_password_put");
                }
            }
        });
    }

    /**
     * Generates a pin for user's to be able to join a household
     * @param view Generate Pin Household textview that was clicked
     */
    public void onGenerateClicked(View view) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.house_pin, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        Button okButton = (Button) dialogView.findViewById(R.id.ok_button);
        final TextView pin_txt = (TextView) dialogView.findViewById(R.id.pin_text);
        final AlertDialog alertDialog = builder.create();

        /**
         * Passing session key
         */
        JSONObject params = new JSONObject();
        try {
            params.put("sessionKey", session);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, PIN_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    if (response.getBoolean("status") == true) { //if household is joined
                        Log.d("Success", response.toString());
                        pin_txt.setText(response.getString("message"));
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.pin_success), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.pin_failure), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Failure", "request encountered an error");
                VolleyLog.d("VolleyError: " + error.getMessage());
            }
        });
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "join_household_put");

        alertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }

    /**
     * Allows a user to create a new household
     * @param view Create Household textview that was clicked
     */
    public void onCreateHouseClicked(View view) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.create_household, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final ProgressDialog pdialog = new ProgressDialog(this);

        Button cancelButton = (Button) dialogView.findViewById(R.id.cancel_button);
        Button addButton = (Button) dialogView.findViewById(R.id.add_button);
        final EditText house_txt = (EditText) dialogView.findViewById(R.id.household_text);
        final TextView errorMsg = (TextView) dialogView.findViewById(R.id.error_text);

        errorMsg.setVisibility(View.INVISIBLE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                household = house_txt.getText().toString();

                pdialog.setMessage("Loading...");
                pdialog.show();

                /**
                 * Passing household number
                 */
                JSONObject params = new JSONObject();
                try {
                    params.put("username", username);
                    params.put("sessionKey", session);
                    params.put("household", household);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, CREATE_URL, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        pdialog.hide();
                        try {
                            if (response.getBoolean("status") == true) { //if household is joined
                                Log.d("Success", response.toString());
                                alertDialog.cancel();
                                mSharedPreferences.edit().putString(HOUSEHOLD, household).commit();
                                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.house_success), Toast.LENGTH_SHORT);
                                toast.show();

                            } else {
                                errorMsg.setVisibility(View.VISIBLE);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.hide();
                        Log.d("Failure", "request encountered an error");
                        VolleyLog.d("VolleyError: " + error.getMessage());
                    }
                });
                AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "join_household_put");
            }
        });
    }


    /**
     * Adds a user to a particular household
     * @param view Join Household textview that was clicked
     */
    public void onJoinHouseClicked(View view) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.join_household, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final ProgressDialog pdialog = new ProgressDialog(this);

        Button cancelButton = (Button) dialogView.findViewById(R.id.cancel_button);
        Button addButton = (Button) dialogView.findViewById(R.id.add_button);
        final EditText house_txt = (EditText) dialogView.findViewById(R.id.house_text);
        final EditText pin_txt = (EditText) dialogView.findViewById(R.id.pin_text);
        final TextView errorMsg = (TextView) dialogView.findViewById(R.id.error_text);

        errorMsg.setVisibility(View.INVISIBLE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                household = house_txt.getText().toString();
                final String pin = pin_txt.getText().toString();

                pdialog.setMessage("Loading...");
                pdialog.show();

                /**
                 * Passing household number
                 */
                JSONObject params = new JSONObject();
                try {
                    params.put("sessionKey", session);
                    params.put("household", household);
                    params.put("password", pin);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (household.equals("") || pin_txt.equals("")) {
                    errorMsg.setText(getString(R.string.error_field));
                    errorMsg.setVisibility(View.VISIBLE);
                } else {


                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, JOIN_URL, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                            pdialog.hide();
                            try {
                                if (response.getBoolean("status") == true) { //if household is joined
                                    Log.d("Success", response.toString());
                                    alertDialog.cancel();
                                    mSharedPreferences.edit().putString(HOUSEHOLD, household).commit();
                                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.house_success), Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    errorMsg.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pdialog.hide();
                            Log.d("Failure", "request encountered an error");
                            VolleyLog.d("VolleyError: " + error.getMessage());
                        }
                    });
                    AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "join_household_put");
                }
            }
        });
    }

    /**
     * Allows user to leave their household
     * @param view Leave Household textview that was clicked
     */
    public void onLeaveHouseClicked(View view) {

        final ProgressDialog pdialog = new ProgressDialog(this);

        JSONObject params = new JSONObject();
        try {
            params.put("sessionKey", session);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, LEAVE_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                pdialog.hide();
                try {
                    if (response.getBoolean("status") == true) { //if leaving household is
                        Log.d("Success", response.toString());
                        mSharedPreferences.edit().remove(HOUSEHOLD).commit();
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.house_left), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Log.d("Failure", "could not leave household");
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.house_not_left), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdialog.hide();
                Log.d("Failure", "request encountered an error");
                VolleyLog.d("VolleyError: " + error.getMessage());
            }
        });
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "left_household_put");
    }
}