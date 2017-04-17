package com.paweelk.cpvisandroid;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.paweelk.cpvisandroid.model.Server;
import com.paweelk.cpvisandroid.service.ApiService;
import com.paweelk.cpvisandroid.service.ImageTouchedTask;

import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VisualizationActivity extends AppCompatActivity {

    public static final String EXTRA_SERVER_ID = "serverId";
    public static final String EXTRA_SERVER_NAME = "serverName";
    public static final String EXTRA_SERVER_URL = "serverUrl";
    public static final String EXTRA_SERVER_USERNAME = "serverUsername";
    public static final String EXTRA_SERVER_PASSWORD = "serverPassword";
    private Server mServer;
    private ApiService mApiService;
    private ImageView mImageView;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load Setting

        //Load Server
        mServer = new Server();
        mServer.setId(getIntent().getLongExtra(VisualizationActivity.EXTRA_SERVER_ID, 0))
                .setName(getIntent().getStringExtra(VisualizationActivity.EXTRA_SERVER_NAME))
                .setUrl(getIntent().getStringExtra(VisualizationActivity.EXTRA_SERVER_URL))
                .setUsername(getIntent().getStringExtra(VisualizationActivity.EXTRA_SERVER_USERNAME))
                .setPassword(getIntent().getStringExtra(VisualizationActivity.EXTRA_SERVER_PASSWORD));
        Log.i(this.getClass().toString(), "Visualization from server with id: " + String.valueOf(mServer.getId()) + " running");
        //Load API
        mApiService = new ApiService(mServer);
        //Set Content
        setContentView(R.layout.activity_visualization);
        //Load image content from remote server
        mImageView = (ImageView) findViewById(R.id.imageVisualization);
        mImageView.setImageResource(R.drawable.common_google_signin_btn_icon_dark_disabled);
        final Handler handler = new Handler();
        final Runnable changeImage = new Runnable() {
            @Override
            public void run() {
                Log.i(VisualizationActivity.class.toString(), "Image changing");
                try {
                    Bitmap bitmap = mApiService.getBitmapStream();
                    mImageView.setImageBitmap(bitmap);
                    handler.postDelayed(this, 1000);
                } catch (Exception e) {
                    Log.e(getClass().toString(), "Exception ", e);
                    new AlertDialog.Builder(VisualizationActivity.this).setTitle(R.string.title_alert_server)
                            .setMessage(e.getMessage())
                            .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Nothing to do
                        }
                    }).show();
                }
            }
        };
        handler.postDelayed(changeImage, 1000);
        //On touch listener creating
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int[] params = new int[2];
                    params[0] = (int) event.getX();
                    params[1] = (int) event.getY();
                    new ImageTouchedTask(VisualizationActivity.this, mApiService).execute(params);
                    return true;
                }
                return VisualizationActivity.super.onTouchEvent(event);
            }
        });
        //Full scren
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


}
