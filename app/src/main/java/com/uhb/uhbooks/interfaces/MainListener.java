package com.uhb.uhbooks.interfaces;

import com.uhb.uhbooks.models.User;

public interface MainListener {

    void onLoading(boolean loading);

    void onLoginSuccess(User user, String token);

}
