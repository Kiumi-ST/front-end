package com.example.kiumi

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("survey")
    suspend fun submitSurvey(@Body surveyData: SurveyData): Response<SurveyResponse>
}