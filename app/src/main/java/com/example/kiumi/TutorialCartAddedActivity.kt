package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kiumi.R
import com.example.kiumi.TutorialMainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TutorialCartAddedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial_cart_added)

        // Coroutine을 사용하여 3초 후에 TutorialMainActivity로 이동
        lifecycleScope.launch {
            delay(2000) // 3초 대기
            val intent = Intent(this@TutorialCartAddedActivity, TutorialMainActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }
    }
}
