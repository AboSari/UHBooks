package com.uhb.uhbooks;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uhb.uhbooks.adapters.StatePagerAdapter;
import com.uhb.uhbooks.fragments.PhotoViewFragment;
import com.uhb.uhbooks.models.File;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends AppCompatActivity {

    private static final String TAG = "PhotoViewActivity";
    public static final String IMAGES = "images";

    // widgets
    private ViewPager viewPager;
    private TextView tvNum;

    // vars
    private List<String> mImages;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        viewPager = findViewById(R.id.viewPager_photoView);
        tvNum = findViewById(R.id.tvNumber_photoView);

        getImages();
        if (mImages != null) {
            init();
        }
    }

    private void init() {
        List<Fragment> fragments = new ArrayList<>();
        for (String image : mImages) {
            PhotoViewFragment fragment = PhotoViewFragment.getInstance(image);
            fragments.add(fragment);
        }

        StatePagerAdapter adapter = new StatePagerAdapter(getSupportFragmentManager(), fragments);

        setNumber(position);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void getImages() {
        if (getIntent().hasExtra(IMAGES)) {
            Gson gson = new Gson();

            String json = getIntent().getStringExtra(IMAGES);
            Type type = new TypeToken<File>() {
            }.getType();

            File file = gson.fromJson(json, type);
            mImages = file.getImagePathList();
        }
    }

    private ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            setNumber(position);
        }
    };

    private void setNumber(int position) {
        tvNum.setText((position + 1) + " - " + mImages.size());
    }

}
