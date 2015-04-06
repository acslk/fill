package com.example.david.motion.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.david.motion.R;
import com.example.david.motion.activities.LevelActivity;

/**
 * Created by David on 2015-03-30.
 */
public class StageFragment extends Fragment implements View.OnClickListener {

    SharedPreferences preferences;
    String title;
    boolean completed;
    int nextLevel;

    RelativeLayout nextLevelView;
    RelativeLayout allLevelView;
    TextView nextLevelTextView;

    public static StageFragment newInstance (String title) {
        StageFragment fragment = new StageFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stage, container, false);
        title = getArguments().getString("title", "defaultTitle");
        ((TextView)view.findViewById(R.id.stage_title)).setText(title);

        nextLevelView = (RelativeLayout)view.findViewById(R.id.nextLevel);
        nextLevelTextView = (TextView)view.findViewById(R.id.nextLevelText);
        allLevelView = (RelativeLayout)view.findViewById(R.id.allLevels);

        nextLevelView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrievePreference();
        updateContent();
        Log.i("Motion", "Stage Fragment " + title + " Resumed");
    }

    public void retrievePreference () {
        completed = preferences.getBoolean(title+"_completed", false);
        nextLevel = preferences.getInt(title+"_nextLevel", 1);
    }

    public void updateContent() {
        if (!completed) {
            nextLevelView.setVisibility(View.VISIBLE);
            allLevelView.setVisibility(View.GONE);
            nextLevelTextView.setText("Continue to Level " + nextLevel);
        } else {
            nextLevelView.setVisibility(View.GONE);
            allLevelView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextLevel:
                ((LevelActivity)getActivity()).startGame(v);
                break;
        }
    }
}
