package com.taraba.gulfoilapp.network;

import android.util.Base64;

import com.taraba.gulfoilapp.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dell on 28-Jan-19.
 */
public class ServiceBuilder {

    public static String BASE_URL = BuildConfig.BASE_URL;
    public static String source = BuildConfig.AUTH_USERNAME + ":" + BuildConfig.AUTH_PASSWORD;
    private static String ret = "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);

    private static Interceptor headerInterceptor = new Interceptor() {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("Authorization", ret).build();

            Response response = chain.proceed(request);
            return response;
        }
    };


    //Create OK HTTP
    private static OkHttpClient okhttp = new OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build();
    //Create Retrofit Builder
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BASE_URL).client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    //Create Retrofit Instance
    private static Retrofit retrofit = retrofitBuilder.build();

    public static Retrofit getRetrofit() {
        return retrofit;
    }


    public static boolean isSuccess(String status) {
        return status != null && (status.equals("success") || status.equals("sucess"));
    }
}
