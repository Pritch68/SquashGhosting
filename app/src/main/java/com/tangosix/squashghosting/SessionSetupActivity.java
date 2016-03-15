package com.tangosix.squashghosting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Map;
import java.util.Set;

public class SessionSetupActivity extends AppCompatActivity {
    public final static String RALLIES_MESSAGE = "com.tangosix.squashghosting.RALLIES_MESSAGE";
    public final static String SHOTSPERRALLY_MESSAGE = "com.tangosix.squashghosting.SHOTSPERRALLY_MESSAGE";
    public final static String SHOTINTERVAL_MESSAGE = "com.tangosix.squashghosting.SHOTINTERVAL_MESSAGE";
    public final static String BREAK_MESSAGE = "com.tangosix.squashghosting.BREAK_MESSAGE";
    public final static String SOUND_ENABLED_MESSAGE = "com.tangosix.squashghosting.SOUND_ENABLED_MESSAGE";
    public final static String RALLY_COUNTER_ENABLED_MESSAGE = "com.tangosix.squashghosting.RALLY_COUNTER_ENABLED_MESSAGE";

    private int Rallies = 8;
    private int ShotsPerRally = 15;
    private int ShotInterval = 4500;
    private double ShotIntervalDisplay = 3.5;
    private int Break = 15;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Load saved values for Rallies, ShotsPerRally, ShotInterval, and Break
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Rallies = sharedPref.getInt(getString(R.string.saved_Rallies), 8);
        ShotsPerRally = sharedPref.getInt(getString(R.string.saved_ShotsPerRally), 15);
        ShotInterval = sharedPref.getInt(getString(R.string.saved_ShotInterval), 4500);
        Break = sharedPref.getInt(getString(R.string.saved_Break), 15);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                sendMessage();
            }
        });

        // Rallies
        final TextView textViewRallies = (TextView) findViewById(R.id.textViewRallies);

        SeekBar seekBarRallies = (SeekBar) findViewById(R.id.seekBarRallies);

        // Set value from Preferences
        seekBarRallies.setProgress(Rallies-3);
        textViewRallies.setText(String.format("%d", Rallies));

        seekBarRallies.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBarRallies) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBarRallies) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBarRallies, int progress, boolean fromUser) {

                Rallies = progress + 3;
                textViewRallies.setText(String.format("%d", Rallies));
            }
        });

        //ShotsPerRally
        final TextView textViewShotsPerRally = (TextView) findViewById(R.id.textViewShotsPerRally);

        SeekBar seekBarShotsPerRally = (SeekBar) findViewById(R.id.seekBarShotsPerRally);

        seekBarShotsPerRally.setProgress(ShotsPerRally-5);
        textViewShotsPerRally.setText(String.format("%d", ShotsPerRally));

        seekBarShotsPerRally.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBarShotsPerRally) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBarShotsPerRally) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBarShotsPerRally, int progress, boolean fromUser) {

                ShotsPerRally = progress + 5;
                textViewShotsPerRally.setText(String.format("%d", ShotsPerRally));
            }
        });

        //ShotInterval
        final TextView textViewShotInterval = (TextView) findViewById(R.id.textViewShotInterval);

        SeekBar seekBarShotInterval = (SeekBar) findViewById(R.id.seekBarShotInterval);

        seekBarShotInterval.setProgress((ShotInterval-2500)/100);
        ShotIntervalDisplay = ((double) ShotInterval) / 1000.0;
        textViewShotInterval.setText(String.format("%.1f", ShotIntervalDisplay));

        seekBarShotInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBarShotInterval) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBarShotInterval) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBarShotInterval, int progress, boolean fromUser) {

                ShotInterval = progress * 100 + 2500;
                ShotIntervalDisplay = ((double) ShotInterval) / 1000.0;
                textViewShotInterval.setText(String.format("%.1f", ShotIntervalDisplay));
            }
        });

        //Break
        final TextView textViewBreak = (TextView) findViewById(R.id.textViewBreak);

        SeekBar seekBarBreak = (SeekBar) findViewById(R.id.seekBarBreak);

        seekBarBreak.setProgress(Break-5);
        textViewBreak.setText(String.format("%d", Break));

        seekBarBreak.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBarBreak) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBarBreak) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBarBreak, int progress, boolean fromUser) {

                Break = progress + 5;
                textViewBreak.setText(String.format("%d", Break));
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_setup, menu);
        return true;
    }

    public void sendMessage() {

        // Retrieve App Settings
        SharedPreferences otherPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean mSoundEnabled = otherPref.getBoolean("pref_sound",true);
        boolean mRallyCounterEnabled = otherPref.getBoolean("pref_rally_counter",false);

        // Save session values
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_Rallies), Rallies);
        editor.putInt(getString(R.string.saved_ShotsPerRally), ShotsPerRally);
        editor.putInt(getString(R.string.saved_ShotInterval), ShotInterval);
        editor.putInt(getString(R.string.saved_Break), Break);
        editor.commit();

        // Start RunSession Activity
        Intent intent = new Intent(this, RunSessionActivity.class);
        intent.putExtra(RALLIES_MESSAGE, Rallies);
        intent.putExtra(SHOTSPERRALLY_MESSAGE, ShotsPerRally);
        intent.putExtra(SHOTINTERVAL_MESSAGE, ShotInterval);
        intent.putExtra(BREAK_MESSAGE, Break);
        intent.putExtra(SOUND_ENABLED_MESSAGE, mSoundEnabled);
        intent.putExtra(RALLY_COUNTER_ENABLED_MESSAGE, mRallyCounterEnabled);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SessionSetup Page", // TODO: Define a title for the content shown.
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
                "SessionSetup Page", // TODO: Define a title for the content shown.
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
