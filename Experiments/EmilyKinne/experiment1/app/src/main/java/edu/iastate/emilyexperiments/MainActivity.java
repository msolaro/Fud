package edu.iastate.emilyexperiments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button joke;
    TextView txt, ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joke = (Button) findViewById(R.id.joke);
        txt = (TextView) findViewById(R.id.text);
        ans = (TextView) findViewById(R.id.answer);
        ans.setVisibility(View.INVISIBLE);

    }

    public void onJokeClicked(View view) {
        ans.setVisibility(View.VISIBLE);
    }
}
