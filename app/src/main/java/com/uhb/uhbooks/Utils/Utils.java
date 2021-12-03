package com.uhb.uhbooks.Utils;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.uhb.uhbooks.R;
import com.uhb.uhbooks.models.Api;

import retrofit2.Response;

public class Utils {

    public static void showResponseFailedDialog(Context context, Response<Api> response) {
        String errorMsg;
        if (!response.isSuccessful()) {
            try {
                assert response.errorBody() != null;
                Api errorBody = new Gson().fromJson(response.errorBody().charStream(), Api.class);
                errorMsg = errorBody.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                errorMsg = context.getString(R.string.unknown_error);
            }
        } else {
            assert response.body() != null;
            errorMsg = response.body().getMessage();
        }

        if (!((Activity) context).isFinishing()) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.error) + " " + response.code() + "!")
                    .setMessage(errorMsg)
                    .create()
                    .show();
        }
    }

    public static void showRetrofitFailureDialog(Context context, Throwable t) {
        String message = t.getMessage();
        if (!((Activity) context).isFinishing()) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.connection_fail))
                    .setMessage(message)
                    .create()
                    .show();
        }
    }

    public static void showInternetUnAvailable(Context context) {
        if (!((Activity) context).isFinishing()) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.internet_not_available))
                    .setMessage(context.getString(R.string.please_confirm_your_connection))
                    .create()
                    .show();
        }
    }
}
