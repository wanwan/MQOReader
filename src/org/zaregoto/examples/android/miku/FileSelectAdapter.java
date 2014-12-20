package org.zaregoto.examples.android.miku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;


public class FileSelectAdapter extends ArrayAdapter {

    private LayoutInflater inflator;

    public FileSelectAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FileSelectAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FileSelectAdapter(Context context, int textViewResourceId, File[] objects) {
        super(context, textViewResourceId, objects);
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FileSelectAdapter(Context context, int resource, int textViewResourceId, File[] objects) {
        super(context, resource, textViewResourceId, objects);
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FileSelectAdapter(Context context, int textViewResourceId, List<File> objects) {
        super(context, textViewResourceId, objects);
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FileSelectAdapter(Context context, int resource, int textViewResourceId, List<File> objects) {
        super(context, resource, textViewResourceId, objects);
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = inflator.inflate(R.layout.filelistitem, null);
        }

        final File item = (File) this.getItem(position);
        TextView main = (TextView) convertView.findViewById(R.id.main);
        TextView detail = (TextView) convertView.findViewById(R.id.detail);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);

        if (null != main && null != item) {
            main.setText(item.getName());
        }
        if (null != detail && null != item) {
            detail.setText(item.getAbsolutePath());
        }


        return convertView;
    }
}
