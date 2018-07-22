package com.sample.leapskills.Retrofit;

import android.content.Context;

import com.sample.leapskills.common.CommonKeyUtility;

import retrofit2.Response;


/**
 * Created by garvit on 20/5/17.
 */


public interface RetrofitTaskListener<M> {
    public void onRetrofitTaskComplete(Response<M> response, Context context, CommonKeyUtility.CallerFunction callerFunction);

    public void onRetrofitTaskFailure(Throwable t, CommonKeyUtility.CallerFunction callerFunction);
}