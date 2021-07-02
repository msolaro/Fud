package edu.iastate.experiment2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;


public class MainActivity2 extends AppCompatActivity {

    TextView name_txt, age_txt;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        String firstName = intent.getStringExtra(MainActivity.NAME);

        name_txt = (TextView) findViewById(R.id.firstName);
        name_txt.setText(firstName);

        mSharedPreferences = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);

        age_txt = (TextView) findViewById(R.id.ages);
        age_txt.setText(mSharedPreferences.getInt(firstName, 0)+" years old!");

    }

}
