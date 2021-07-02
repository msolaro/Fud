package com.example.fud.AppActivities;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fud.Controller.AppController;
import com.example.fud.Models.UserModel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fud.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class allows one to register as a new user
 * @author Emily Kinne
 * @since 3-26-2020
 */
public class RegistrationActivity extends AppCompatActivity {

    /**
     * New User object sent to the database
     */
    private UserModel newUser;
    /**
     * Strings to store the user's statistics to be displayed on the dashboard
     */
    private String firstName, lastName, email, password, confirmation;
    /**
     * Textview for error message if login is unsuccessful
     */
    private TextView errorMsg;
    /**
     * String url for registration
     */
    private String REG_URL;


    /**
     * Creates the view for the registration page. Calls a volley request to register a new user and includes all of the error handling.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final EditText first_txt = (EditText) findViewById(R.id.firstname_text);
        final EditText last_txt = (EditText) findViewById(R.id.lastname_text);
        final EditText email_txt = (EditText) findViewById(R.id.email_text);
        final EditText password_txt = (EditText) findViewById(R.id.password_text);
        final EditText confirmation_txt = (EditText) findViewById(R.id.confirmation_text);

        errorMsg = (TextView) findViewById(R.id.error_text) ;
        errorMsg.setVisibility(View.INVISIBLE);

        REG_URL = getString(R.string.URL)+"registration";

        final ProgressDialog pDialog = new ProgressDialog(this);

        Button register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = first_txt.getText().toString();
                lastName = last_txt.getText().toString();
                email = email_txt.getText().toString();
                password = password_txt.getText().toString();
                confirmation = confirmation_txt.getText().toString();

                /**
                 * Passing new user data
                 */
                JSONObject params = new JSONObject();
                try {

                    params.put("firstName", firstName);
                    params.put("lastName", lastName);
                    params.put("username", email);
                    params.put("password", password);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                /**
                 * Checks that all fields are filled and passwords match
                 */
                if (firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || confirmation.equals("")) {
                    errorMsg.setText(getString(R.string.error_register));
                    errorMsg.setVisibility(View.VISIBLE);
                }
                else if(!password.equals(confirmation)) {
                    errorMsg.setText(getString(R.string.error_match));
                    errorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    newUser = new UserModel(firstName, lastName, email, password);
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, REG_URL, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Success", response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("Registration Activity", "Error: " + error.getMessage());
                        }
                    }) {
                    };
                    //Adds request to the queue and opens the login activity
                    AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "new_user_post");
                    openLogin();
                }
            }
        });
    }

    /**
     * Method to open the Login activity when registration is successful.
     */
    private void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}