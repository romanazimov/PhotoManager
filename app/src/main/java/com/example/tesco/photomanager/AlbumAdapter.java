package com.example.tesco.photomanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tecso.photomanager.R;

import java.util.List;

public class AlbumAdapter extends BaseAdapter {
    private Context context;
    private final List<FolderInfo> foldersNameList;


    public AlbumAdapter(Context context, List<FolderInfo> foldersList) {
        this.context = context;
        this.foldersNameList = foldersList;

    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View gridView = inflater.inflate(R.layout.album_grid_item, null);

        // set image based on selected text
        ImageView imageView = gridView.findViewById(R.id.album_image);
        TextView albumName = gridView.findViewById(R.id.album_name);
        imageView.setImageResource(R.drawable.folder_icon);
        albumName.setText(foldersNameList.get(position).name);

        return gridView;
    }

    @Override
    public int getCount() {
        return foldersNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return foldersNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}