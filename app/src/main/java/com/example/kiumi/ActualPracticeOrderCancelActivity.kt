package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ActualPracticeOrderCancelActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_order_cancel)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        findViewById<Button>(R.id.button_no).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.button_yes).setOnClickListener {
            CartManager.clearCart()
            PointManager.resetPoint()
            val intent = Intent(this@ActualPracticeOrderCancelActivity, ActualPracticeActivity::class.java)
                .apply { putExtra("previous_activity", "실전 연습_주문 취소") }
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        startTime = System.currentTimeMillis()
    }

    override fun onStop() {
        super.onStop()
        endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        val params = Bundle().apply {
            putLong("screen_duration", duration)
            putString("screen_name", "실전 연습_실전 연습_주문 취소")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }
}