package com.example.experiment1.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.widget.Button;
import android.widget.Toast;

import com.example.experiment1.FragmentInteractionListener;
import com.example.experiment1.R;
import com.example.experiment1.ui.tools.ToolsFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button helloWorldButton;
    private Button switchFragment;
    private FragmentInteractionListener mFragmentInteractionListener;
    private Toast toast;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        helloWorldButton = (Button) root.findViewById(R.id.Hello_Wrld_Button);
        helloWorldButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helloWorld(v);
            }
        });
        switchFragment = (Button) root.findViewById(R.id.switch_button_2);
        switchFragment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mFragmentInteractionListener.switchFragment(2, v);
            }
        });
        toast = Toast.makeText(getActivity().getApplicationContext(), "Hello World", Toast.LENGTH_SHORT);

        return root;
    }

    public void helloWorld(View view) {

        toast.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentInteractionListener = (FragmentInteractionListener) activity;
    }


}