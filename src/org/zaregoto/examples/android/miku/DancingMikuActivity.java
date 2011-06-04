package org.zaregoto.examples.android.miku;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.zaregoto.examples.android.miku.parser.mqo.MetaseqData;
import org.zaregoto.examples.android.miku.parser.mqo.MetaseqParser;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class DancingMikuActivity extends Activity {
	
	private static final String loadfile = "primitive/cube02.mqo";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);

        AssetManager assets = getResources().getAssets();
        InputStream is = null;
        try {
			is = assets.open(loadfile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        MetaseqParser parser = null;
		parser = new MetaseqParser(is);
        MetaseqData data = parser.parse();
        
        MQORenderer renderer = new MQORenderer(this, data);
        
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(renderer);
        
        setContentView(glSurfaceView);
        
        return;
    }

}