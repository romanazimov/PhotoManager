package com.example.tesco.photomanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.tecso.photomanager.R;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderActivity extends AppCompatActivity {


    ViewPager pager;
    ViewPagerAdapter adapter;
    public static List<ImageInfo> Images = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_slide_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        pager = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        DBClass dbClass = new DBClass(getApplicationContext());
        String id = getIntent().getExtras().getString("folderID");
        Images = dbClass.getAllImages(id);
        pager.setAdapter(adapter);

    }


}
