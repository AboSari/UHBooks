package com.uhb.uhbooks.fragments;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.uhb.uhbooks.interfaces.MainListener;
import com.uhb.uhbooks.models.Api;

import retrofit2.Call;


public class mainFragment extends Fragment {

    MainListener mainListener;
    Call<Api> mCall;

    public void loading(boolean b) {
        mainListener.onLoading(b);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainListener = (MainListener) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCall != null) mCall.cancel();
    }
}
