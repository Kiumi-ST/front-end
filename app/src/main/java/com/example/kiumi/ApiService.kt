package com.example.kiumi

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("survey")
    suspend fun submitSurvey(@Body surveyData: SurveyData): Response<SurveyResponse>

    @POST("/votingresult")
    suspend fun submitVotingResult(@Body surveyData: VotingRequest): Response<VotingResponse>

    @Multipart
    @POST("/analyze-deepface")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("screen_name") screenName: RequestBody
    ): Call<AnalyzeResponse>
}