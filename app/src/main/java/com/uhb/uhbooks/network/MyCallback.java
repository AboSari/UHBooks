package com.uhb.uhbooks.network;

import com.uhb.uhbooks.models.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MyCallback implements Callback<Api> {
    @Override
    public void onResponse(Call<Api> call, Response<Api> response) {

    }

    @Override
    public void onFailure(Call<Api> call, Throwable t) {

    }
}
