package com.tony.helen.flick;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

public class SpeakActivity extends Activity {

    ImageView settings_iv;
    TextView gesture_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);

    }

    @Override
    protected void onResume() {
        super.onResume();

        settings_iv = (ImageView) findViewById(R.id.settings_iv);
        gesture_tv = (TextView) findViewById(R.id.gesture_tv);

        settings_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
