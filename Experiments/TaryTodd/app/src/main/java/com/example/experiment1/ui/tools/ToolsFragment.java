package com.example.experiment1.ui.tools;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.experiment1.FragmentInteractionListener;
import com.example.experiment1.R;

import java.util.Random;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    private Button mGetFunky;
    private Button mSwitchFragment;
    private boolean buttonPressed = false;
    private int currentColor = 0x000000;
    private FragmentInteractionListener mFragmentInteractionListener;
    private Random random = new Random();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mGetFunky = (Button) root.findViewById(R.id.funky_button);
        mGetFunky.setOnClickListener(new View.OnClickListener() {
            public void onClick(View root) {
                    getFunky(root);
            }
        });
        mSwitchFragment = (Button) root.findViewById(R.id.switch_button);
        mSwitchFragment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mFragmentInteractionListener.switchFragment(1, v);
            }
        });

        return root;
    }

    public void getFunky(View v) {
//        Timer t = new Timer()
//        int min = 0x000000;
//        int max = 0xFFFFFF;
//
//        if (buttonPressed) {
//            buttonPressed = false;
//        } else {
//            buttonPressed = true;
//        }
//
//        while (buttonPressed) {
//            if (currentColor >= max || currentColor < min) {
//                currentColor = min;
//            }
//            currentColor++;
//            v.setBackgroundColor(currentColor);

            int min = 0x00;
            int max = 0xFF;
            v.getRootView().setBackgroundColor(Color.rgb(random.nextInt(max+1), random.nextInt(max+1), random.nextInt(max+1)));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentInteractionListener = (FragmentInteractionListener) activity;
    }
}