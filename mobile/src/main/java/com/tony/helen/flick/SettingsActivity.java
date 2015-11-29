package com.tony.helen.flick;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
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

    GestureManager gestureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        gestureManager = GestureManager.getInstance(getApplicationContext());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#00ffffff"));

        phrase_tv1_1 = (TextView) findViewById(R.id.phrase_tv1_1);
        phrase_tv1_2 = (TextView) findViewById(R.id.phrase_tv1_2);
        phrase_tv1_3 = (TextView) findViewById(R.id.phrase_tv1_3);
        phrase_tv1_4 = (TextView) findViewById(R.id.phrase_tv1_4);

        phrase_tv2_1 = (TextView) findViewById(R.id.phrase_tv2_1);
        phrase_tv2_2 = (TextView) findViewById(R.id.phrase_tv2_2);
        phrase_tv2_3 = (TextView) findViewById(R.id.phrase_tv2_3);
        phrase_tv2_4 = (TextView) findViewById(R.id.phrase_tv2_4);

        phrase_tv1_1.setText("Where is the washroom");
        phrase_tv1_2.setText("Excuse me");
        phrase_tv1_3.setText("Hello");
        phrase_tv1_4.setText("My name is Tony");

        phrase_tv2_1.setText("Someone help me");
        phrase_tv2_2.setText("Bill please");
        phrase_tv2_3.setText("Can I get a coffee please");
        phrase_tv2_4.setText("When is the next bus coming");

        phrase_tv1_1.setOnClickListener(clickFactory(GestureManager.Gesture.FRONT_FIST));
        phrase_tv1_2.setOnClickListener(clickFactory(GestureManager.Gesture.FRONT_SPREAD));
        phrase_tv1_3.setOnClickListener(clickFactory(GestureManager.Gesture.FRONT_OUT));
        phrase_tv1_4.setOnClickListener(clickFactory(GestureManager.Gesture.FRONT_IN));

        phrase_tv2_1.setOnClickListener(clickFactory(GestureManager.Gesture.DOWN_FIST));
        phrase_tv2_2.setOnClickListener(clickFactory(GestureManager.Gesture.DOWN_SPREAD));
        phrase_tv2_3.setOnClickListener(clickFactory(GestureManager.Gesture.DOWN_OUT));
        phrase_tv2_4.setOnClickListener(clickFactory(GestureManager.Gesture.DOWN_IN));
    }

    public View.OnClickListener clickFactory(final GestureManager.Gesture gesture) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context cont = getApplicationContext();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final EditText input = new EditText(cont);
                        input.setTextColor(Color.BLACK);
                        input.setGravity(Gravity.CENTER_HORIZONTAL);
                        new AlertDialog.Builder(SettingsActivity.this)
                                .setTitle("Change phrase associated with gesture")
                                .setMessage("")
                                .setView(input)
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Hide keyboard
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(input.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                                        dialog.cancel();
                                    }
                                })
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Hide keyboard
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(input.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                        gestureManager.setPhrase(gesture, input.getText().toString());
                                    }
                                }).create().show();
                    }
                });
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

}
