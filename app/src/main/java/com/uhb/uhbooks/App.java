package com.uhb.uhbooks;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.uhb.uhbooks.Utils.SessionManager;
import com.uhb.uhbooks.api.ApiInterface;
import com.uhb.uhbooks.models.Api;
import com.uhb.uhbooks.network.Interceptor.NetworkConnectionInterceptor;
import com.uhb.uhbooks.network.InternetConnectionListener;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String TAG = "App";

    private ApiInterface mApiInterface;
    private SessionManager mSession;

    private InternetConnectionListener mInternetConnectionListener;
    private SessionManager.SessionListener mSessionListener;

    public void setInternetConnectionListener(InternetConnectionListener listener) {
        mInternetConnectionListener = listener;
    }

    public void removeInternetConnectionListener() {
        mInternetConnectionListener = null;
    }

    public void setSessionListener(SessionManager.SessionListener listener) {
        mSessionListener = listener;
    }

    public void removeSessionListener() {
        mSessionListener = null;
    }

    // ---------------------------------------------------------------------------------- \\

    @Override
    public void onCreate() {
        super.onCreate();

        mSession = SessionManager.getInstance(getApplicationContext());
        if (isInternetAvailable() && mSession.isLoggedIn()) {
            checkToken();
        }
    }

    private void checkToken() {
        Log.d(TAG, "checkToken: called");

        if (mSession.getToken().equals("")) {
            Log.d(TAG, "checkToken: mSession.getToken().equals(\"\")");
            return;
        }

        Call<Api> call = getApiInterface().checkToken(mSession.getToken());
        call.enqueue(new Callback<Api>() {
            @Override
            public void onResponse(Call<Api> call, retrofit2.Response<Api> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Token Invalid!");
                    mSession.clearUserSettings();
                    if (mSessionListener != null) mSessionListener.onTokenInvalid();
                }
            }

            @Override
            public void onFailure(Call<Api> call, Throwable t) {
            }
        });

    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        okHttpClientBuilder.connectTimeout(15, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(15, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(15, TimeUnit.SECONDS);
        okHttpClientBuilder.addInterceptor(getNetworkConnectionInterceptor());
        return okHttpClientBuilder.build();

    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ApiInterface getApiInterface() {
        if (mApiInterface == null) {
            mApiInterface = getRetrofit().create(ApiInterface.class);
        }
        return mApiInterface;
    }

    private NetworkConnectionInterceptor getNetworkConnectionInterceptor() {
        return new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return App.this.isInternetAvailable();
            }

            @Override
            public void onInternetUnavailable() {
                // we can broadcast this event to activity/fragment/service
                // through LocalBroadcastReceiver or
                // RxBus/EventBus
                // also we can call our own interface method
                // like this.
                if (mInternetConnectionListener != null) {
                    mInternetConnectionListener.onInternetUnavailable();
                }
            }
        };
    }

    public SessionManager.SessionListener getSessionListener() {
        return mSessionListener;
    }

    public SessionManager getSession() {
        return mSession;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
