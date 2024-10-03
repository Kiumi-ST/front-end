package com.test.kiumi.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Spring boot 서버 연동
    private const val SPRING_BOOT_BASE_URL = "http://43.202.235.227:8080/"

    val springBootRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SPRING_BOOT_BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val springBootApiService: SpringBootApiService = springBootRetrofit.create(SpringBootApiService::class.java)

    // Flask 서버 연동
    private const val FLASK_BASE_URL = "https://kiumi-ef992.du.r.appspot.com"

    val flaskRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(FLASK_BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val flaskApiService: FlaskApiService = flaskRetrofit.create(FlaskApiService::class.java)

}