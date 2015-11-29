package com.tony.helen.flick;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity implements Animation.AnimationListener, GestureManager.GestureListener {
    GestureManager gestureManager;

    // External Communication

    private int checkEngineStatus;

    // Activity Layout
    TextToSpeech textEngine;

    ImageButton speech_btn;
    ImageView logo_img;
    TextView instruction_tv;
    ImageView settings_iv;
    ImageView sync_iv;
    ImageView blanket;

    String myoInput;

    Animation animZoomIn;
    Animation animFadeOut;
    boolean onPage;

    @Override
    public void onAnimationEnd(Animation animation) {
        //Animation ended
        if (animation.toString().equals(animZoomIn.toString())) {
            blanket.setVisibility(View.VISIBLE);
            onPage = false;
            logo_img.setVisibility(View.GONE);
            //View view = findViewById(R.id.mainView);
            //view.setBackgroundColor(Color.parseColor("#905778db"));
            Intent intent  = new Intent(getApplicationContext(), SpeakActivity.class);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Animation is repeating
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Animation started
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blanket = (ImageView) findViewById(R.id.blanket);
        onPage = false;
        gestureManager = GestureManager.getInstance(this);
        gestureManager.setListener(this);
        //onScanActionSelected();
    }

    @Override
    protected void onResume() {
        super.onResume();

        myoInput = "hello world";

        speech_btn = (ImageButton) findViewById(R.id.speak_Btn);
        logo_img = (ImageView) findViewById(R.id.logo_img);
        instruction_tv = (TextView) findViewById(R.id.instructions_tv);
        settings_iv = (ImageView) findViewById(R.id.settings_iv);
        sync_iv = (ImageView) findViewById(R.id.sync_iv);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#00ffffff"));

        getActionBar().hide();

        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        animZoomIn.setAnimationListener(this);

        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        animFadeOut.setAnimationListener(this);

        settings_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPage = false;
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        sync_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPage = false;
                gestureManager = GestureManager.getInstance(getApplicationContext());
                //onScanActionSelected();
                Intent intent = new Intent(getApplicationContext(), CalibrateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

//        speech_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                speech_btn.startAnimation(animZoomIn);
//                logo_img.startAnimation(animFadeOut);
//                instruction_tv.startAnimation(animFadeOut);
//                Intent intent = new Intent(getApplicationContext(), SpeakActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    public void unlockSpeech() {

        speech_btn.startAnimation(animZoomIn);
        logo_img.startAnimation(animFadeOut);
        instruction_tv.startAnimation(animFadeOut);

        //textEngine.speak(myoInput, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Activity is gone, so unregister the listener.
        //gestureManager.destroyListener();
        if (isFinishing()) {
            // The Activity is finishing, so shutdown the Hub. This will disconnect from the Myo.
            gestureManager.finishHub();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        blanket.setVisibility(View.GONE);
        logo_img.setVisibility(View.VISIBLE);
        onPage = true;
        // Check which request we're responding to
        if (requestCode == 1 || requestCode == 2) {
            gestureManager.setListener(this);
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

    @Override
    public void onNewGesture(GestureManager.Gesture newGesture) {
        Log.d("myo", "work PLEASE");
        if (onPage && newGesture == GestureManager.Gesture.UNLOCK) {
            onPage = false;
            Log.d("myo", "STARTED SPEAK");
            unlockSpeech();
        }
    }

    @Override
    public void onConnected() {

    }
}
