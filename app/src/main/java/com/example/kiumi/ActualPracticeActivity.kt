package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

class ActualPracticeActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            firebaseAnalytics.logEvent("QR_code_scan"){
                param(FirebaseAnalytics.Param.CONTENT, "QR_code_scan")
            }

            val intent = Intent(
                this@ActualPracticeActivity,
                ActualPracticeQRSuccess::class.java
            )
            startActivity(intent)
        }

        // 주문하기 버튼 클릭 시
        findViewById<Button>(R.id.button_order).setOnClickListener {
            firebaseAnalytics.logEvent("button_click_order"){
                param(FirebaseAnalytics.Param.CONTENT, "order")
            }
            val intent = Intent(
                this@ActualPracticeActivity,
                ActualPracticePlaceSelectionActivity::class.java
            )
            startActivity(intent)
        }

        // 언어 버튼 클릭 시
        findViewById<Button>(R.id.button_language).setOnClickListener {
        }

        // 도움 버튼 클릭 시
        findViewById<Button>(R.id.button_help).setOnClickListener {
        }
    }
}