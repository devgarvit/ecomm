package com.sample.leapskills.Retrofit;


import com.sample.leapskills.Entities.Item;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by garvit on 20/5/17.
 */
public interface RetrofitInterface<M> {

    @GET
    Call<Item> getItems(@Url String url);
}
