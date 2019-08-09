package com.martin.stackusers.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by martinmistery on 10/29/18.
 */

class RetrofitClient {
    companion object {
        private const val BASE_URL = "https://api.stackexchange.com/2.0/"
    }

    fun getService(): APIRequest {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(APIRequest::class.java)
    }
}