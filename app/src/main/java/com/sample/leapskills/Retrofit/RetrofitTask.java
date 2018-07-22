package com.sample.leapskills.Retrofit;

import android.content.Context;

import com.sample.leapskills.Persistent.TinyDB;
import com.sample.leapskills.common.CommonKeyUtility;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by garvit on 20/5/17.
 */

public class RetrofitTask<M> implements Callback<M> {

    private RetrofitTaskListener callback;
    private Context mContext;
    private CommonKeyUtility.HTTP_REQUEST_TYPE _methodType;
    private CommonKeyUtility.CallerFunction mCallerFunction;
    private String URL;
    private Object postParamsEntity;
//    private boolean toRunOnMainThread = false;

    public RetrofitTask(RetrofitTaskListener<M> callback,
                        CommonKeyUtility.HTTP_REQUEST_TYPE methodType, CommonKeyUtility.CallerFunction callerFunction,
                        String url, Context context) {
        this.callback = callback;
        this._methodType = methodType;
        this.URL = url;
        mContext = context;
        this.mCallerFunction = callerFunction;
    }

    public RetrofitTask(RetrofitTaskListener<M> callback,
                        CommonKeyUtility.HTTP_REQUEST_TYPE methodType, CommonKeyUtility.CallerFunction callerFunction,
                        String url, Context context, Object postParamsEntity) {
        this.callback = callback;
        this._methodType = methodType;
        mContext = context;
        this.mCallerFunction = callerFunction;
        this.postParamsEntity = postParamsEntity;
        this.URL = url;
    }


    public void execute(final boolean isAuthorized) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request request;
                                if (isAuthorized)
                                    request = chain.request().newBuilder()
                                            .addHeader("Content-Type", "application/json")
                                            .addHeader("Authorization", new TinyDB(mContext).getString("accessToken"))
                                            .build();
                                else
                                    request = chain.request().newBuilder()
                                            .addHeader("Content-Type", "application/json").build();
                                return chain.proceed(request);
                            }
                        }).build();


        RetrofitInterface service = RetrofitClient.getClient(httpClient).create(RetrofitInterface.class);

        Call call = null;
        switch (mCallerFunction) {
            case GET_ITEMS:
                call = service.getItems(URL);
                break;
        }


        if (call != null) {
            try {
                call.enqueue(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponse(Call<M> call, retrofit2.Response<M> response) {
        callback.onRetrofitTaskComplete(response, mContext, mCallerFunction);

    }

    @Override
    public void onFailure(Call<M> call, Throwable t) {
        callback.onRetrofitTaskFailure(t, mCallerFunction);
    }


}
