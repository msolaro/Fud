package com.example.fud.MainActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fud.AppActivities.LoginActivity;
import com.example.fud.R;

import java.text.DateFormat;
import java.util.Calendar;

import static com.example.fud.AppActivities.MainActivity.FIRSTNAME;


public class HomeFragment extends Fragment {

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
     * This method loads shared preferences for the app
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSharedPreferences = context.getSharedPreferences(getString(R.string.package_name), Context.MODE_PRIVATE);
    }

    /**
     * Creates the home page view by loading the user information and data and time
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_home, container, false);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView name = (TextView) v.findViewById(R.id.first_name);
        String myName = mSharedPreferences.getString(FIRSTNAME, null);
        name.setText(" " + myName);

        TextView textViewDate = v.findViewById(R.id.date_txt);
        textViewDate.setText(currentDate);

        return v;
    }
}
