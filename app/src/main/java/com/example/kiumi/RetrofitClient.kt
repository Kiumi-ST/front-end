package com.example.kiumi

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Spring boot 서버 연동
    private const val SPRING_BOOT_BASE_URL = "http://192.168.0.8:8080/" // http://(본인 노트북 IPv4 주소):8080/

    val springBootRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SPRING_BOOT_BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val springBootApiService: SpringBootApiService = springBootRetrofit.create(SpringBootApiService::class.java)

    // Flask 서버 연동
    private const val FLASK_BASE_URL = "http://192.168.0.8:8000/"

    val flaskRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(FLASK_BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val flaskApiService: FlaskApiService = flaskRetrofit.create(FlaskApiService::class.java)

}