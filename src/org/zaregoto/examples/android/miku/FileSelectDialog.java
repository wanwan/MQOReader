package org.zaregoto.examples.android.miku;

import android.app.DialogFragment;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;


public class FileSelectDialog extends DialogFragment {

    static FileSelectDialog newInstance(ArrayList<File> files) {
        FileSelectDialog f = new FileSelectDialog();

        Bundle args = new Bundle();
        args.putSerializable("files", files);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
