package com.motion.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.motion.R;
import com.motion.fragments.LevelFragment;
import com.motion.fragments.ResultFragment;

public class LevelActivity extends FullScreenActivity {

    static final int GAME_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, new LevelFragment());
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

    public void startGame(View view) {
        Log.i("Motion", "startGame intent");
        startActivityForResult(new Intent(this, GameActivity.class), GAME_REQUEST);
        overridePendingTransition(R.animator.fadeout, R.animator.fadein);
    }

    public void popTopFragment (View view) {
        getFragmentManager().popBackStack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GAME_REQUEST) {
            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof ResultFragment) {
                getFragmentManager().popBackStack();
            }
            if (resultCode == RESULT_OK) {
                ResultFragment resultFragment = ResultFragment.newInstance(data);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, resultFragment);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                transaction.commit();
            }
        }
    }
}
