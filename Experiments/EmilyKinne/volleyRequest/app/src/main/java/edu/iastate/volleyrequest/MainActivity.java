package edu.iastate.volleyrequest;

import edu.iastate.volleyrequest.AppController;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    /**
     * Textview for error message if login is unsuccessful
     */
    TextView message;

    String url ="http://10.24.226.237:8080/";
    /**
     * String for debugging
     */
    private static final String TAG = "Volley Test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);
        message = (TextView) findViewById(R.id.Message) ;
        message.setVisibility(View.INVISIBLE);

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        StringRequest strReq =new StringRequest(Request.Method.GET,"http://10.24.226.237:8080/",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                message.setText(response.toString());
                message.setVisibility(View.VISIBLE);
                pDialog.hide();
            }
        }, new Response.ErrorListener(){
            @Override public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,"Error: "+ error.getMessage()); // hide the progress dialog
                pDialog.hide();
            }
        });
        queue.add(strReq);
    }
}
