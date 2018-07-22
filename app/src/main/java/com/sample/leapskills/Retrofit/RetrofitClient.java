package com.sample.leapskills.Retrofit;


import com.sample.leapskills.Utils.NetworkUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by garvit on 20/5/17.
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(OkHttpClient httpClient) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }
}
