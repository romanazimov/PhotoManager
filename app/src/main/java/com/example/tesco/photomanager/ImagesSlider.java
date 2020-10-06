package com.example.tesco.photomanager;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tecso.photomanager.R;

public class ImagesSlider extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.images_slider, container, false);
        ImageView imageView = view.findViewById(R.id.slide_image);

        assert getArguments() != null;
        int position = getArguments().getInt("pos", 0);
        Uri uri = Uri.parse(ImageSliderActivity.Images.get(position).path);
        imageView.setImageURI(uri);
        return view;
    }
}
