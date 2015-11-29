package com.tony.helen.flick;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    TextView phrase_tv1_1;
    TextView phrase_tv1_2;
    TextView phrase_tv1_3;
    TextView phrase_tv1_4;

    TextView phrase_tv2_1;
    TextView phrase_tv2_2;
    TextView phrase_tv2_3;
    TextView phrase_tv2_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        phrase_tv1_1 = (TextView) findViewById(R.id.phrase_tv1_1);
        phrase_tv1_2 = (TextView) findViewById(R.id.phrase_tv1_2);
        phrase_tv1_3 = (TextView) findViewById(R.id.phrase_tv1_3);
        phrase_tv1_4 = (TextView) findViewById(R.id.phrase_tv1_4);

        phrase_tv2_1 = (TextView) findViewById(R.id.phrase_tv2_1);
        phrase_tv2_2 = (TextView) findViewById(R.id.phrase_tv2_2);
        phrase_tv2_3 = (TextView) findViewById(R.id.phrase_tv2_3);
        phrase_tv2_4 = (TextView) findViewById(R.id.phrase_tv2_4);



    }

    @Override
    protected void onResume() {
        super.onResume();



    }

}
