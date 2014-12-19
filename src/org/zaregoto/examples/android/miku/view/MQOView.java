package org.zaregoto.examples.android.miku.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;


public class MQOView extends GLSurfaceView {

    public MQOView(Context context) {
        this(context, null);
    }

    public MQOView(Context context, AttributeSet attrs) {
        super(context, attrs);

        MQORenderer renderer = new MQORenderer();
        setRenderer(renderer);
    }


}
