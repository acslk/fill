package com.example.david.motion.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.david.motion.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2015-03-30.
 */
public class LevelFragment extends Fragment implements View.OnClickListener {

    private ViewPager stagePager;
    private ScreenSlidePagerAdapter stageAdapter;
    List<StageFragment> stageFragments = new ArrayList<>();

    SharedPreferences preferences;
    int stagesUnlocked;

    ImageView leftArrow, rightArrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_level, container, false);

        stagePager = (ViewPager)v.findViewById(R.id.stage_pager);
        stageAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        stagePager.setAdapter(stageAdapter);

        leftArrow = (ImageView)v.findViewById(R.id.arrow_left);
        rightArrow = (ImageView)v.findViewById(R.id.arrow_right);
        leftArrow.setOnClickListener(this);
        rightArrow.setOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrievePreference();
        stageAdapter.notifyDataSetChanged();
    }

    public void retrievePreference () {
        stagesUnlocked = preferences.getInt("stagesUnlocked", 2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_left:
                Log.i("Motion", "left arrow " + stagePager.getCurrentItem());
                if (stagePager.getCurrentItem() > 0)
                    stagePager.setCurrentItem(stagePager.getCurrentItem() - 1);
                break;
            case R.id.arrow_right:
                if (stagePager.getCurrentItem() < stageAdapter.getCount() - 1)
                    stagePager.setCurrentItem(stagePager.getCurrentItem() + 1);
                break;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (stageFragments.size() <= position) {
                stageFragments.add(StageFragment.newInstance("Stage" + (position + 1)));
            }
            return stageFragments.get(position);
        }

        @Override
        public int getCount() {
            return stagesUnlocked;
        }
    }
}
