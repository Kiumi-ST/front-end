package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class TutorialThankYou : PopupActivity() {

    private var isTTSActive: Boolean = false // 기본값 설정

    // Handler와 Runnable을 클래스 변수로 정의
    private val handler = Handler(Looper.getMainLooper())
    private val navigateRunnable = Runnable {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 스플래시 액티비티를 종료하여 뒤로가기 버튼을 누를 때 다시 이 액티비티로 돌아오지 않도록 함
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_thank_you)

        // 이전 액티비티에서 전달된 isTTSActive 값 받아오기 (기본값 false)
        isTTSActive = intent.getBooleanExtra("isTTSActive", false)

        // 1.5초 후에 다음 액티비티로 이동
        handler.postDelayed(navigateRunnable, 1500)
    }
}