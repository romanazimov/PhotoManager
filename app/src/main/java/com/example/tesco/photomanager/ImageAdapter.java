package com.example.tesco.photomanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tecso.photomanager.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final List<ImageInfo> imagesList;


    public ImageAdapter(Context context, List<ImageInfo> mImageList) {
        this.context = context;
        this.imagesList = mImageList;


    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View gridView = inflater.inflate(R.layout.image_grid_item, null);
        ImageView imageView = gridView.findViewById(R.id.image);
        TextView imageName = gridView.findViewById(R.id.image_name);
        imageName.setText(imagesList.get(position).caption);
        Uri imageUri = Uri.parse(imagesList.get(position).path);
        imageView.setImageURI(imageUri);
        return gridView;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}