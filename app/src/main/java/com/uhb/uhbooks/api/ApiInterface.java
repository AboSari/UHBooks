package com.uhb.uhbooks.api;

import com.uhb.uhbooks.models.Api;
import com.uhb.uhbooks.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    String BASE_URL = "http://10.59.100.166:5000/";
//    String BASE_URL = "https://uhbooks.herokuapp.com/";
    String AUTH_HEADER = "x-access-token";


    // Auth
    @POST("api/login")
    Call<Api> login(@Body User user);

//    @POST("api/login")
//    Call<Api> login(@Body Map<String, String> fields);

    // Users
    @GET("api/users/{id}")
    Call<Api> getUser(@Path("id") int id);

    // Items
    @GET("api/items")
    Call<Api> getItems(@Query("level") String type, @Header(AUTH_HEADER) String token);

    @GET("api/items")
        // search
    Call<Api> getFolders(@Query("q") String q, @Header(AUTH_HEADER) String token);

    @GET("api/check-token")
    Call<Api> checkToken(@Header(AUTH_HEADER) String token);

    @GET("api/image/{path}")
    Call<Api> getImages(@Path("path") String path, @Header(AUTH_HEADER) String token);
}
