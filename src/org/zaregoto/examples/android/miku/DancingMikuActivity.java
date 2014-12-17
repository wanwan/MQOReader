package org.zaregoto.examples.android.miku;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import org.zaregoto.examples.android.miku.view.MQORenderer;
import org.zaregoto.mqoparser.model.MQOData;
import org.zaregoto.mqoparser.parser.MQOParser;
import org.zaregoto.mqoparser.parser.exception.LoadStateException;
import org.zaregoto.mqoparser.parser.exception.StateTransferException;
import org.zaregoto.mqoparser.util.LogUtil;

import java.io.*;

public class DancingMikuActivity extends Activity {
	
	private static final String loadfile = "primitive/cube02.mqo";
    private static final String SAMPLEFILE = "samplefile.mqo";
    private static final int BUFSIZE = 2048;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);

        try {
            copyAssertsToFile(loadfile, SAMPLEFILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //MQORenderer renderer = new MQORenderer(this, null);
        MQORenderer renderer = new MQORenderer();
        
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(renderer);
        
        setContentView(glSurfaceView);
        
        return;
    }


    private void copyAssertsToFile(String assertFile, String localFile) throws IOException {

        AssetManager assets = getResources().getAssets();
        InputStream is = null;
        FileOutputStream os = null;
        byte[] buf = new byte[BUFSIZE];
        try {
            is = assets.open(assertFile);
            os = openFileOutput(localFile, Context.MODE_PRIVATE);
            int sz;
            while (0 < (sz = is.read(buf))) {
                os.write(buf, 0, sz);
            }
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }

        return;
    }


    private MQOData readData(String filename) throws IOException, StateTransferException {

        MQOParser parser = new MQOParser();
        MQOData data = null;
        try {
            parser.open(filename);

            data = parser.parse();

            LogUtil.d(data.toString());

        } catch (LoadStateException e) {
            e.printStackTrace();
        } finally {
            try {
                parser.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

}