package com.example.tesco.photomanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        ImagesSlider slider = new ImagesSlider();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        slider.setArguments(bundle);
        return slider;
    }

    @Override
    public int getCount() {
        if (ImageSliderActivity.Images != null) {
            return ImageSliderActivity.Images.size();
        } else {
            return 0;
        }

    }
}
