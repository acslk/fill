package com.example.david.motion.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.david.motion.R;
import com.example.david.motion.activities.GameActivity;

/**
 * Created by David on 2015-03-28.
 */
public class PauseDialogFragment extends DialogFragment implements View.OnClickListener {

    boolean quit = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setCanceledOnTouchOutside(false);

        View pauseView = inflater.inflate(R.layout.dialog_pause, container, false);
        Button quitButton = (Button)pauseView.findViewById(R.id.quit_button);
        Button resumeButton = (Button)pauseView.findViewById(R.id.resume_button);
        Button restartButton = (Button)pauseView.findViewById(R.id.restart_button);
        quitButton.setOnClickListener(this);
        resumeButton.setOnClickListener(this);
        restartButton.setOnClickListener(this);

        return pauseView;
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        return dialog;
//    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!quit && getActivity() != null) {
            ((GameActivity) getActivity()).resumeGame();
        }
    }

    @Override
    public void onClick(View v) {
        int button_id = v.getId();
        if (button_id == R.id.quit_button) {
            quit = true;
            ((GameActivity) getActivity()).endGame(true);
        } else if (button_id == R.id.resume_button) {
            Log.i("Motion", "dismiss dialog");
            dismiss();
        } else if (button_id == R.id.restart_button) {
            Log.i("Motion", "dismiss dialog");
            dismiss();
        }
    }
}
