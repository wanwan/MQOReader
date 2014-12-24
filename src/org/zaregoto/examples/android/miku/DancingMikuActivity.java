package org.zaregoto.examples.android.miku;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.zaregoto.examples.android.miku.view.MQORenderer;
import org.zaregoto.mqoparser.model.MQOData;
import org.zaregoto.mqoparser.parser.MQOParser;
import org.zaregoto.mqoparser.parser.exception.LoadStateException;
import org.zaregoto.mqoparser.parser.exception.StateTransferException;
import org.zaregoto.mqoparser.util.LogUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DancingMikuActivity extends Activity implements FileSelectDialogFragment.FileSelect {
	
    private static final int BUFSIZE = 2048;
    private static final String[] ASSETS_DATA_FOLDER = {"primitive", "medaka", "hachune"};

    private DialogFragment fileSelectDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);

        File outputdir;

        try {
            for (String dir: ASSETS_DATA_FOLDER) {
                outputdir = new File(getCacheDir(), dir);
                copyAssertsToLocal(dir, outputdir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ArrayList<File> mqos = new ArrayList<File>();
        fileSearch(getCacheDir(), ".*.mqo", mqos);

        Iterator<File> it = mqos.iterator();
        while (it.hasNext()) {
            File f = it.next();
            System.out.println("mqofile: " + f.getAbsolutePath());
        }

        //MQORenderer renderer = new MQORenderer(this, null);
        MQORenderer renderer = new MQORenderer();
        
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(renderer);
        
        //setContentView(glSurfaceView);
        setContentView(R.layout.main);

        Button openBtn = (Button) findViewById(R.id.openBtn);
        if (null != openBtn) {
            openBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileSelectDialog = showFileSelectDialog(mqos);
                }
            });
        }
    }



    private void copyAssertsToLocal(String folder, File dest) throws IOException {

        AssetManager assets = getResources().getAssets();
        InputStream is = null;
        FileOutputStream os = null;
        byte[] buf = new byte[BUFSIZE];
        try {
            File assetsdir = new File(folder);
            File outputdir;

            for (String path: assets.list(folder)) {
                is = assets.open(String.valueOf(new File(assetsdir, path)));
                if (null != is) {
                    File output = new File(dest, path);
                    if (!output.exists()) {
                        dest.mkdirs();
                        dest.mkdir();
                        output.createNewFile();
                    }
                    os = new FileOutputStream(output);
                    int sz;
                    while (0 < (sz = is.read(buf))) {
                        os.write(buf, 0, sz);
                    }
                }
            }

            //is = assets.open(assertFile);
            //os = openFileOutput(localFile, Context.MODE_PRIVATE);
            //int sz;
            //while (0 < (sz = is.read(buf))) {
            //    os.write(buf, 0, sz);
            //}
        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
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


    private void fileSearch(File dir, String regex, ArrayList<File> array) {

        Pattern p;

        if (null == array || null == regex) {
            return;
        }
        else {
            p = Pattern.compile(regex);
        }

        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                fileSearch(f, regex, array);
            }
            else {
                Matcher m = p.matcher(f.getName());
                if (m.find()) {
                    array.add(f);
                }
            }
        }
    }


    int mStackLevel = 0;
    DialogFragment showFileSelectDialog(ArrayList<File> files) {
        //mStackLevel++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = FileSelectDialogFragment.newInstance(DialogFragment.STYLE_NO_FRAME, files);
        newFragment.show(ft, "dialog");
        return newFragment;
    }

    @Override
    public void fileSelected(File f) {
        TextView statusLabel = (TextView) findViewById(R.id.statusLbl);
        if (null != statusLabel) {
            statusLabel.setText(f.getName());
            statusLabel.invalidate();
        }
        if (null != fileSelectDialog) {
            fileSelectDialog.dismiss();
            fileSelectDialog = null;
        }
    }


    private MQOData parseMQO(File mqoFile) {
        MQOParser parser;
        MQOData data = null;

        if (null != mqoFile && mqoFile.exists()) {
            parser = new MQOParser();
            try {
                parser.open(mqoFile.getAbsolutePath());
                data = parser.parse();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (LoadStateException e) {
                e.printStackTrace();
            } catch (StateTransferException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }
}