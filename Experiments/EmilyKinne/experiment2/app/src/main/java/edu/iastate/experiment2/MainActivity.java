package edu.iastate.experiment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class MainActivity extends AppCompatActivity {

    EditText firstName;
    public static final String NAME = "firstName";
    String name;
    DatePicker date;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = findViewById(R.id.name);

        date = (DatePicker) findViewById(R.id.datePicker);

        mSharedPreferences = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        mSharedPreferences.edit().clear().commit();

        Button enter = (Button) findViewById(R.id.enter_button);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = firstName.getText().toString();
                int age = getAge(date);
                mSharedPreferences.edit().putInt(name, age).commit();
                onEnterClicked(name);
            }
        });
    }

    public void onEnterClicked(String name){
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra(NAME, name);
        startActivity(intent);
    }

    public int getAge(DatePicker date){

        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - date.getYear() - 1;
        if (currentMonth > date.getMonth()){
            age++;
        }
        if (currentMonth == date.getMonth()) {
            if(currentDay >= date.getDayOfMonth()){
                age++;
            }
        }
        return age;
    }

}
