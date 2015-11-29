package com.tony.helen.flick;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.thalmic.myo.Hub;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class SpeakActivity extends Activity implements GestureManager.GestureListener{

    ImageView settings_iv;
    //TextView gesture_tv;
    GestureManager manager;
    TextToSpeech textEngine;
    String audioPath;
    Visualizer visual;
    MediaPlayer voicePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speak);
        settings_iv = (ImageView) findViewById(R.id.settings_iv);
//        gesture_tv = (TextView) findViewById(R.id.gesture_tv);

        manager = GestureManager.getInstance(this);
        manager.setListener(this);

        textEngine = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textEngine.setLanguage(Locale.CANADA);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        settings_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onNewGesture(GestureManager.Gesture newGesture) {

        if (newGesture == GestureManager.Gesture.UNLOCK) {
            Log.d("myo", "unlock speak");
            setResult(Activity.RESULT_OK);
            finish();
            return;
        } else if (newGesture != GestureManager.Gesture.FIST && newGesture != GestureManager.Gesture.LOCK) {
            Log.d("myo", manager.getPhrase(newGesture));
            //gesture_tv.setText(manager.getPhrase(newGesture));

            Typewriter gesture_tv = new Typewriter(this);
            gesture_tv = (Typewriter) findViewById(R.id.gesture_tv);

            gesture_tv.setCharacterDelay(30);
            gesture_tv.animateText(manager.getPhrase(newGesture));

            textEngine.speak(manager.getPhrase(newGesture), TextToSpeech.QUEUE_FLUSH, null);

        }
    }

    @Override
    public void onConnected() {

    }
}
