package com.example.fud.AppActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fud.Controller.AppController;
import com.example.fud.R;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is responsible for logging in the user
 */
public class LoginActivity extends AppCompatActivity {

    private String username, password, session;
    /**
     * Textview used to display an error message if login is entered incorrectly
     */
    private TextView errorMsg;
    /**
     * Shared Preferences used to automatically save the user's email and session key
     */
    private SharedPreferences mSharedPreferences;
    /**
     * String url for login
     */
    private String LOG_URL;
    /**
     * String for storing username in sharedPreferences
     */
    public static String USR_KEY = "user key";
    /**
     * String for storing user's session in sharedPreferences
     */
    public static String SESH_KEY = "session key";

    /**
     * This method is responsible for logging the user into the service. This is accomplished by making volley requests in Android.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Sets up the text edit views and error message
        final EditText username_txt = (EditText) findViewById(R.id.username_text);
        final EditText password_txt = (EditText) findViewById(R.id.password_text);
        errorMsg = (TextView) findViewById(R.id.error_text);
        errorMsg.setVisibility(View.INVISIBLE);

        LOG_URL = getString(R.string.URL)+"login";

        mSharedPreferences = getSharedPreferences(getString(R.string.package_name), Context.MODE_PRIVATE);
        //mSharedPreferences.edit().clear().commit();

        if (mSharedPreferences.getString(SESH_KEY, null)!=null){
            openMain(mSharedPreferences.getString(SESH_KEY, null));
        }

        final ProgressDialog pdialog = new ProgressDialog(this);

        TextView registerText = (TextView) findViewById(R.id.register_text);
        registerText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openRegistration();
            }
        });

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = username_txt.getText().toString();
                password = password_txt.getText().toString();
                //Check that all fields are complete
                if (username.equals("") || password.equals("")) {
                    errorMsg.setText(getString(R.string.error_login));
                    errorMsg.setVisibility(View.VISIBLE);
                }
                else{
                    pdialog.setMessage("Loading...");
                    pdialog.show();

                    JSONObject params = new JSONObject();
                    try {
                        params.put("username", username);
                        params.put("password", password);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Request a string response from the provided URL.
                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, LOG_URL, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("PDialog", response.toString());
                            pdialog.hide();
                            try { //if password is incorrect
                                if (response.getString("token").equals("false")) { //if password is incorrect
                                    Log.d("FAILURE", response.toString());
                                    errorMsg.setText(getString(R.string.error_valid));
                                    errorMsg.setVisibility(View.VISIBLE);
                                }
                                else{ //if password is correct
                                    session = response.getString("token");
                                    mSharedPreferences.edit().putString(SESH_KEY, session).commit();
                                    mSharedPreferences.edit().putString(USR_KEY, username).commit();
                                    openMain(response.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Failure", "request encountered an error");
                            Log.d("Volley", error.toString());
                            errorMsg.setText(getString(R.string.error_valid));
                            errorMsg.setVisibility(View.VISIBLE);
                            pdialog.hide();
                            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.unsuccessful), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                    // Add the request to the RequestQueue.
                    AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
                }
            }
        });
    }

    /**
     * Method to open the Main activity when login is successful.
     */
    private void openMain(String session) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(SESH_KEY, session);
        startActivity(intent);
        finish();
    }

    /**
     * Method to open the Registration Activity when text is clicked
     */
    private void openRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
