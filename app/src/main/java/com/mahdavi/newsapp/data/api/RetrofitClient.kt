package com.mahdavi.newsapp.data.api

import com.mahdavi.newsapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private var instance: Api? = null

        @Synchronized
        fun getInstance(): Api {
            if (instance == null)
                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BuildConfig.BASE_URL)
                    .build()
                    .create(Api::class.java)
            return instance as Api
        }
    }
}