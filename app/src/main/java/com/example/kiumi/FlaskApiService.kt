package com.example.kiumi

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FlaskApiService {
    @Multipart
    @POST("analyze-deepface")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("screen_name") screenName: RequestBody
    ): Call<AnalyzeResponse>
}