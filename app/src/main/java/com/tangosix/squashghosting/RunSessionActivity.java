package com.tangosix.squashghosting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RunSessionActivity extends AppCompatActivity {
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
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private int Rallies = 8;
    private int ShotsPerRally = 15;
    private int ShotInterval = 6;
    private int Break = 15;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    TextView tempView;
    ImageView imgView;
    int x;
    Random shotRand = new Random();
    String number;
    int currentShot;
    Handler shotHandler = new Handler();
    Runnable runSession;
    SoundPool mySound;
    int shotSoundId;
    int beepSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_run_session);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        // findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        Intent intent = getIntent();

        mySound = new SoundPool(1,AudioManager.STREAM_NOTIFICATION,0);
        shotSoundId = mySound.load(this, R.raw.shot, 1);
        beepSoundId = mySound.load(this, R.raw.beeplow, 1);

        Rallies = intent.getIntExtra(SessionSetupActivity.RALLIES_MESSAGE, 8);
        ShotsPerRally = intent.getIntExtra(SessionSetupActivity.SHOTSPERRALLY_MESSAGE, 15);
        ShotInterval = intent.getIntExtra(SessionSetupActivity.SHOTINTERVAL_MESSAGE, 4500);
        Break = intent.getIntExtra(SessionSetupActivity.BREAK_MESSAGE, 15);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(1000);
        start_session();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    /**
    @Override
    protected void onPause() {
        shotHandler.removeCallbacks(runSession);
        super.onPause();
    }

    @Override
    protected void onResume() {
        shotHandler.postDelayed(runSession,5000);
        super.onResume();
    }
    **/

    // show the shot
    public void display_shot(int shot) {
        switch ( shot ) {
            case 0:
                imgView = (ImageView) findViewById(R.id.shotOne);
                imgView.setVisibility(View.INVISIBLE);
                imgView = (ImageView) findViewById(R.id.shotTwo);
                imgView.setVisibility(View.INVISIBLE);
                imgView = (ImageView) findViewById(R.id.shotThree);
                imgView.setVisibility(View.INVISIBLE);
                imgView = (ImageView) findViewById(R.id.shotFour);
                imgView.setVisibility(View.INVISIBLE);
                imgView = (ImageView) findViewById(R.id.shotFive);
                imgView.setVisibility(View.INVISIBLE);
                imgView = (ImageView) findViewById(R.id.shotSix);
                imgView.setVisibility(View.INVISIBLE);
                break;
            case 1:
                imgView = (ImageView) findViewById(R.id.shotOne);
                imgView.setVisibility(View.VISIBLE);
                mySound.play(shotSoundId,1.0f,1.0f,1,0,1.0f);
                break;
            case 2:
                imgView = (ImageView) findViewById(R.id.shotTwo);
                imgView.setVisibility(View.VISIBLE);
                mySound.play(shotSoundId,1.0f,1.0f,1,0,1.0f);
                break;
            case 3:
                imgView = (ImageView) findViewById(R.id.shotThree);
                imgView.setVisibility(View.VISIBLE);
                mySound.play(shotSoundId,1.0f,1.0f,1,0,1.0f);
                break;
            case 4:
                imgView = (ImageView) findViewById(R.id.shotFour);
                imgView.setVisibility(View.VISIBLE);
                mySound.play(shotSoundId,1.0f,1.0f,1,0,1.0f);
                break;
            case 5:
                imgView = (ImageView) findViewById(R.id.shotFive);
                imgView.setVisibility(View.VISIBLE);
                mySound.play(shotSoundId,1.0f,1.0f,1,0,1.0f);
                break;
            case 6:
                imgView = (ImageView) findViewById(R.id.shotSix);
                imgView.setVisibility(View.VISIBLE);
                mySound.play(shotSoundId,1.0f,1.0f,1,0,1.0f);
                break;
        }
    }

    public void start_session() {
        runSession = new Runnable() {
            @Override
            public void run() {
                sequenceSession();
            }
        };
        new Thread(runSession).start();
    }

    private void sequenceSession() {
        // Countdown to Start
        for (int j = 10; j>0; j--){
            currentShot = j;
            if (j<=3) {
                shotHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tempView = (TextView) findViewById(R.id.fullscreen_content);
                        tempView.setText(String.valueOf(String.valueOf(currentShot)));
                        mySound.play(beepSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
                    }
                });
            }
            else {
                shotHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tempView = (TextView) findViewById(R.id.fullscreen_content);
                        tempView.setText(String.valueOf(String.valueOf(currentShot)));
                    }
                });
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        shotHandler.post(new Runnable() {
            @Override
            public void run() {
                tempView = (TextView) findViewById(R.id.fullscreen_content );
                tempView.setText("");
            }
        });

        // For each Rally
        for (int Rally = 1; Rally <= Rallies; Rally++){
            // For each Shot
            for (int Shot=1; Shot <= ShotsPerRally; Shot++){
                // Generate Shot
                currentShot = shotRand.nextInt(6)+1;
                // Display Shot
                shotHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        display_shot(currentShot);
                    }
                });
                // Pause for ShotInterval
                try {
                    Thread.sleep(ShotInterval-800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Clear shot display
                shotHandler.post(new Runnable() {
                    @Override
                    public void run() { display_shot(0); }
                });

                //Pause for 200ms with no shot displayed
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            if (Rally < Rallies) {
                //Pause for break
                for (int j = Break; j > 0; j--) {
                    currentShot = j;
                    if (j <= 3) {
                        shotHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tempView = (TextView) findViewById(R.id.fullscreen_content);
                                tempView.setText(String.valueOf(String.valueOf(currentShot)));
                                mySound.play(beepSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
                            }
                        });
                    }
                    else {
                        shotHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tempView = (TextView) findViewById(R.id.fullscreen_content);
                                tempView.setText(String.valueOf(String.valueOf(currentShot)));
                            }
                        });
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    shotHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tempView = (TextView) findViewById(R.id.fullscreen_content );
                            tempView.setText("");
                        }
                    });

                }
            }

            else {
                shotHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tempView = (TextView) findViewById(R.id.fullscreen_content);
                        tempView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,100);
                        tempView.setText("Done");
                    }
                });
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RunSession Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.tangosix.squashghosting/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RunSession Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.tangosix.squashghosting/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
