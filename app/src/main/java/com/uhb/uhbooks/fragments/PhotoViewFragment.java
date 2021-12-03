package com.uhb.uhbooks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.uhb.uhbooks.R;
import com.uhb.uhbooks.api.ApiInterface;

public class PhotoViewFragment extends Fragment {

    private static final String TAG = "PhotoViewFragment";
    private static final String KEY = "image";

    // widgets
    private PhotoView photoView;

    // vars
    private String mImage;

    public static PhotoViewFragment getInstance(String image) {
        PhotoViewFragment fragment = new PhotoViewFragment();

        if (image != null) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY, image);
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImage = getArguments().getString(KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photoview, container, false);
        photoView = view.findViewById(R.id.photoView);

        init();
        return view;
    }

    private void init() {
        if (mImage != null) {
            Glide.with(getActivity())
                    .load(ApiInterface.BASE_URL + mImage)
                    .into(photoView);
        }
    }
}
