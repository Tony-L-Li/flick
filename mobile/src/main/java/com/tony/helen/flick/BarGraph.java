package com.tony.helen.flick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tli on 2015-11-29.
 */
public class BarGraph extends View {
    int maxWidth;
    int maxHeight;
    float offset;
    ArrayList<Float> bars;
    float offInc = 0.05f;
    int totalBars = 20;
    public BarGraph(Context context, AttributeSet attrs) {
        super(context);

        bars = new ArrayList<Float>();

        offset = 0;
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float variance = 0.4f;
        maxWidth = w;
        maxHeight = h;
        if (bars.size() < totalBars) {
            for (int i = 0; i < totalBars; i ++) {
                if (bars.size() == 0) {
                    bars.add((float) (maxHeight * 0.5));
                } else {
                    float factor = bars.get(0) / maxHeight;
                    bars.add(0,bars.get(0) + randInt((int) (-maxHeight * variance * (factor)), (int) (maxHeight * variance * (1 - factor))));
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float variance = 0.4f;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(255, 87, 120 ,219);
        canvas.drawColor(0);
        for (int i = 0; i < Math.min(bars.size(), totalBars); i ++) {
            float height = bars.get(i);
            if (i == 0 && bars.size() > 1) {
                height = bars.get(i);
            } else if (bars.size() > 1){
                height = offset * bars.get(i-1) + (1-offset) * bars.get(i);
            }
            canvas.drawRect(i * maxWidth / (float)totalBars,
                    height,
                    (i + 1) * maxWidth / (float)totalBars,
                    maxHeight,
                    paint);
        }
        offset += offInc;
        if (offset > 1) {
            if (bars.size() == 0) {
                bars.add((float) (maxHeight * 0.5));
            } else {
                float factor = bars.get(0) / maxHeight;
                bars.add(0,bars.get(0) + randInt((int) (-maxHeight * variance * (factor)), (int) (maxHeight * variance * (1 - factor))));
            }
            offset =0;
        }

        invalidate();
    }

}
