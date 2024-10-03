package com.test.kiumi.data

data class SurveyData(
    val q1: Float,
    val q2: String,
    val q3: String,
    val q4: String,
    val q5: String
)

data class SurveyResponse(
    val isSuccess: Boolean,
    val msg: String
)

data class VotingRequest(
    val question1: String,
    val question2: String,
    val question3: String
)

data class VotingResponse(
    val isSuccess: Boolean,
    val msg: String
)