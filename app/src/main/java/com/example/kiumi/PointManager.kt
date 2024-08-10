package com.example.kiumi

object PointManager {
    private var pointEarned: Boolean = false

    fun setPointEarned(earned: Boolean) {
        pointEarned = earned
    }

    fun isPointEarned(): Boolean {
        return pointEarned
    }

    fun resetPoint() {
        pointEarned = false
    }
}