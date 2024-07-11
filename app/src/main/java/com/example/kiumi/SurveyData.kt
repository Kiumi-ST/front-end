package com.example.kiumi

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