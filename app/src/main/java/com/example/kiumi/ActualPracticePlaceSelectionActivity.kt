package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

class ActualPracticePlaceSelectionActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_place_selection)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            val intent = Intent(
                this@ActualPracticePlaceSelectionActivity,
                ActualPracticeQRSuccess::class.java
            )
            startActivity(intent)
        }

        // 매장 식사 버튼 클릭 시
        findViewById<LinearLayout>(R.id.button_dine_in).setOnClickListener {
            firebaseAnalytics.logEvent("select_dining_option"){
                param(FirebaseAnalytics.Param.CONTENT, "dine_in")
            }
            startActivity(Intent(this, ActualPracticeMainActivity::class.java))
        }

        // 포장 버튼 클릭 시
        findViewById<LinearLayout>(R.id.button_take_out).setOnClickListener {
            firebaseAnalytics.logEvent("select_dining_option"){
                param(FirebaseAnalytics.Param.CONTENT, "take_out")
            }
            startActivity(Intent(this, ActualPracticeMainActivity::class.java))
        }

        // 처음으로 버튼 클릭 시
        findViewById<Button>(R.id.buttonHome).setOnClickListener {
        }

        // 도움 기능 버튼 클릭 시
        findViewById<LinearLayout>(R.id.linearLayoutHelp).setOnClickListener {
        }

        // 영어 버튼 클릭 시
        findViewById<Button>(R.id.button_english).setOnClickListener {
        }

        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            PointManager.setPointEarned(true)
            val intent = Intent(this@ActualPracticePlaceSelectionActivity, ActualPracticeQRSuccess::class.java)
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
            putString("screen_name", "실전 연습_장소")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

}