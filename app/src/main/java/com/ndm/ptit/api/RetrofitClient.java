package com.ndm.ptit.api;

import static android.content.Context.MODE_PRIVATE;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ndm.ptit.utils.Utils;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Interceptor để log trạng thái HTTP
            Interceptor statusInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    return response;
                }
            };

            // Interceptor cho log body
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // Log chi tiết body
                    .addInterceptor(statusInterceptor)  // Log HTTP status
                    .build();

//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(chain -> {
//                        String token = getSharedPreferences("user_prefs", MODE_PRIVATE)
//                                .getString("token", null);
//                        Request request = chain.request()
//                                .newBuilder()
//                                .addHeader("Authorization", "Bearer " + token)
//                                .build();
//                        return chain.proceed(request);
//                    })
//                    .addInterceptor(interceptor)
//                    .build();


            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
