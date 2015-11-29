package com.tony.helen.flick;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by tli on 2015-11-29.
 */
public class BarGraph extends View {
    int maxWidth;
    int maxHeight;
    public BarGraph(Context context) {
        super(context);
    }

    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxWidth = w;
        maxHeight = h;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        invalidate();
    }

}
