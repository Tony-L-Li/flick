package com.tony.helen.flick;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import com.thalmic.myo.scanner.ScanActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {
    GestureManager gestureManager;

    // External Communication

    private int checkEngineStatus;

    // Activity Layout
    TextToSpeech textEngine;

    ImageButton speech_btn;

    String myoInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureManager = new GestureManager(this);
        //onScanActionSelected();

        final ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_custom_view_home);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        Intent intent = new Intent(this, CalibrateActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        myoInput = "hello world";

        speech_btn = (ImageButton) findViewById(R.id.speak_Btn);

        getActionBar().setElevation(0);

        textEngine = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textEngine.setLanguage(Locale.CANADA);
                }
            }
        });

        speech_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), myoInput, Toast.LENGTH_SHORT).show();
                textEngine.speak(myoInput, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Activity is gone, so unregister the listener.
        gestureManager.destroyListener();
        if (isFinishing()) {
            // The Activity is finishing, so shutdown the Hub. This will disconnect from the Myo.
            gestureManager.finishHub();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    private void onScanActionSelected() {
        // Launch the ScanActivity to scan for Myos to connect to.
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }
}
