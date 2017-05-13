package com.tony.helen.flick;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CalibrateActivity extends Activity implements GestureManager.GestureListener{
    private Float[][] calibrations;
    private int currentStage;
    private GestureManager manager;
    private TextView instruct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);
        instruct = (TextView) findViewById(R.id.instructions);
        manager = GestureManager.getInstance(this);
        manager.unlock();
        manager.setListener(this);
        currentStage = 0;
        calibrations = new Float[2][3];
    }

    @Override
    public void onNewGesture(GestureManager.Gesture newGesture) {
        if (newGesture == GestureManager.Gesture.FIST) {
            Float[] temp = manager.getGyro();

            for (int i = 0; i < 3; i++) {
                calibrations[currentStage][i] = temp[i];
                Log.d("myo", Float.toString(temp[i]));
            }
            if (currentStage == 0) {
                instruct.setText("Please lower your hand and make a fist");
            } else if (currentStage == 1) {
                manager.setGyroCalibrations(calibrations);
                manager.lock();
                finish();
            }
            currentStage++;
        }
    }

    @Override
    public void onConnected() {
        instruct.setText("Please hold your hand in front of you and make a fist");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calibrate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
