package com.tony.helen.flick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import com.thalmic.myo.scanner.ScanActivity;
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

    String myoInput;

    Animation animZoomIn;
    Animation animFadeOut;

    @Override
    public void onAnimationEnd(Animation animation) {
        //Animation ended
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

        gestureManager = GestureManager.getInstance(this);
        //onScanActionSelected();
        Intent intent = new Intent(this, CalibrateActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        myoInput = "hello world";

        speech_btn = (ImageButton) findViewById(R.id.speak_Btn);
        logo_img = (ImageView) findViewById(R.id.logo_img);
        instruction_tv = (TextView) findViewById(R.id.instructions_tv);

        getActionBar().hide();

        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        animZoomIn.setAnimationListener(this);

        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        animFadeOut.setAnimationListener(this);

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
                speech_btn.startAnimation(animZoomIn);
                logo_img.startAnimation(animFadeOut);
                instruction_tv.startAnimation(animFadeOut);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
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
    private void onScanActionSelected() {
        // Launch the ScanActivity to scan for Myos to connect to.
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNewGesture(int newGesture) {
        Log.d("myo", "it works");
    }
}
