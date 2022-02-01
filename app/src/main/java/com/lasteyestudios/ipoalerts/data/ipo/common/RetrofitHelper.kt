package com.lasteyestudios.ipoalerts.data.ipo.common

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitHelper(private var BaseUrl: String) {


    fun getInstance(): Retrofit {
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
//        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BaseUrl)
//            .client(client)
            .build()
    }
}