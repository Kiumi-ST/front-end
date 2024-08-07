package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ActualPracticeQRSuccess : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_qrsuccess)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 확인 버튼을 ID로 찾기
        val buttonConfirm: Button = findViewById(R.id.buttonConfirm)

        // 버튼에 클릭 리스너 설정
        buttonConfirm.setOnClickListener {
            // ActualPracticePaymentSelection 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, ActualPracticeMainActivity::class.java)
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
            putString("screen_name", "실전 연습_QR 코드 스캔 시")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

}
