package com.test.kiumi.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SpringBootApiService {
    @POST("survey")
    suspend fun submitSurvey(@Body surveyData: SurveyData): Response<SurveyResponse>

    @POST("votingresult")
    suspend fun submitVotingResult(@Body surveyData: VotingRequest): Response<VotingResponse>
}