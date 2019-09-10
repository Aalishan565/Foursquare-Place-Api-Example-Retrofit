package com.foursquareplaceapitest.gatewaymodel;

import com.foursquareplaceapitest.BuildConfig;
import com.foursquareplaceapitest.dtos.searchresponse.SearchVenueResponse;
import com.foursquareplaceapitest.dtos.vanues.VenuesResponse;
import com.foursquareplaceapitest.utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommunicationManager {
    private static CommunicationManager mCommunicationManager;

    private CommunicationManager() {
    }

    public static CommunicationManager getInstance() {
        if (mCommunicationManager == null) {
            mCommunicationManager = new CommunicationManager();
        }
        return mCommunicationManager;
    }

    private RetrofitAPI getInstanceRetrofit(final boolean head) {
        RetrofitAPI api = null;
        String url;
        try {
            url = BuildConfig.BASE_URL;
            if (null != url) {
                OkHttpClient okHttpClient;
                if (head) {
                    okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request().newBuilder()
                                            .addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE).build();
                                    return chain.proceed(request);
                                }
                            })
                            .build();
                } else {
                    okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                api = retrofit.create(RetrofitAPI.class);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return api;
    }

    public Call<VenuesResponse> getVenuesListReq(String latLang) {
        try {
            return getInstanceRetrofit(true).venuesResponse(Constants.CLIENT_ID_VALUE,
                    Constants.CLIENT_SECRET_KEY_VALUE, Constants.VERSION_VALUE, latLang, Constants.LIMIT_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Call<SearchVenueResponse> getSearchVenuesListReq(String latLang, String query) {
        try {
            return getInstanceRetrofit(true).searchVenuesResponse(Constants.CLIENT_ID_VALUE,
                    Constants.CLIENT_SECRET_KEY_VALUE, Constants.VERSION_VALUE, latLang, query, Constants.LIMIT_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}