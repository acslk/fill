package com.motion.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.motion.R;

public class TestActivity extends Activity {

    TestView view;
    View v;

    private class TestView extends SurfaceView implements SurfaceHolder.Callback {

        private NinePatchDrawable d;
        SurfaceHolder surfaceHolder;

        public TestView (Context context) {
            super(context);
//            setZOrderOnTop(true);
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
//            surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            d.setBounds(100, 100, 600, 600);
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.drawColor(getResources().getColor(R.color.background));
            d.draw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new TestView(this);
        setContentView(view);
    }
}

