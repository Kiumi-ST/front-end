package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TutorialCartAddedActivity : AppCompatActivity() {
    private var isTTSActive: Boolean = false // 기본값 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial_cart_added)

        // 이전 액티비티에서 전달된 isTTSActive 값 받아오기 (기본값 false)
        isTTSActive = intent.getBooleanExtra("isTTSActive", false)

        // Coroutine을 사용하여 1.5초 후에 TutorialMainActivity로 이동
        lifecycleScope.launch {
            delay(1500) // 1.5초 대기
            val intent = Intent(this@TutorialCartAddedActivity, TutorialMainActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive) // 다음 액티비티로 isTTSActive 값 전달
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }
    }
}
