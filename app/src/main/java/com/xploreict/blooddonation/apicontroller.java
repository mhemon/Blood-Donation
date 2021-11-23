package com.xploreict.blooddonation;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class apicontroller
{
    static final String url="https://blooddonation.gadgetlab.store";
    private static apicontroller clientobject;
    private static Retrofit retrofit;

    apicontroller()
    {
        retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized apicontroller getInstance()
    {
        if(clientobject==null)
            clientobject=new apicontroller();
        return clientobject;
    }

    public com.xploreict.blooddonation.apiset getapi()
    {
        return retrofit.create(com.xploreict.blooddonation.apiset.class);
    }
}
