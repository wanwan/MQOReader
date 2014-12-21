package org.zaregoto.examples.android.miku;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class FileSelectDialogFragment extends DialogFragment {

    private ArrayList<File> mFiles;

    static FileSelectDialogFragment newInstance(int style, ArrayList<File> files) {
        FileSelectDialogFragment f = new FileSelectDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("files", files);
        args.putInt("style", style);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int _style = getArguments().getInt("num");

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((_style-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((_style-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);

        mFiles = (ArrayList<File>) getArguments().getSerializable("files");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.filelist, container, false);
        ListView lv = (ListView) v.findViewById(R.id.file_list_root);

        //ArrayAdapter<File> adapter = new ArrayAdapter<File>(getActivity(), android.R.layout.simple_list_item_1, mFiles);
        final FileSelectAdapter adapter = new FileSelectAdapter(getActivity(), R.layout.filelistitem, mFiles);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selected = (File) adapter.getItem(position);
                FileSelect selectIf = (FileSelect) getActivity();
                selectIf.fileSelected(selected);
            }
        });
        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public interface FileSelect {
        public void fileSelected(File f);
    }


}
