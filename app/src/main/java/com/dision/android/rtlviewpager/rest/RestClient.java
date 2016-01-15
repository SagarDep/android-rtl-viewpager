package com.dision.android.rtlviewpager.rest;

import com.dision.android.rtlviewpager.constants.ApiConstants;
import com.dision.android.rtlviewpager.rest.service.AppService;

import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

public class RestClient {
    private static final String BASE_URL = ApiConstants.API_BASIC_URL;
    private AppService apiService;

    public RestClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new SimpleXMLConverter())
                .build();

        apiService = restAdapter.create(AppService.class);
    }

    public AppService getAppService() {
        return apiService;
    }

}
