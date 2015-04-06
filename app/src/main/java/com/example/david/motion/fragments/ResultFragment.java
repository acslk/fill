package com.example.david.motion.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.motion.R;

/**
 * Created by David on 2015-03-29.
 */
public class ResultFragment extends Fragment {

    TextView resultText;
    SharedPreferences preferences;

    public static ResultFragment newInstance (Intent intent) {
        ResultFragment resultFragment = new ResultFragment();
        resultFragment.setArguments(intent.getExtras());
        return resultFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        resultText = (TextView)v.findViewById(R.id.result_text);
        Bundle result = getArguments();
        if (result.getBoolean("gameStatus"))
            resultText.setText("Level Completed!");
        else
            resultText.setText(result.getString("failMessage"));
        return v;
    }
}
