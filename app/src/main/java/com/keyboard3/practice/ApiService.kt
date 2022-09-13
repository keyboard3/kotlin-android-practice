package com.keyboard3.practice

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

data class Cat (val name:String,val age: Int,val breed: String);

interface ApiInterface {
    @GET("/next-nest/api/cats")
    suspend fun getCats() : Response<List<Cat>>;
}
val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val client : OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(interceptor)
}.build()

object ApiService {

    val baseUrl = "https://keyboard3.com/"

    fun getInstance(): ApiInterface {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .client(client)
            .build()
            .create(ApiInterface::class.java)
    }
}