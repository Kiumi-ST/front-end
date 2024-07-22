package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kiumi.R

class ActualPracticePickup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_pickup)

        Toast.makeText(this, "5초 동안 화면이 유지됩니다", Toast.LENGTH_LONG).show()

        // 5초 후에 다음 액티비티로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ActualPracticeThankYou::class.java) // NextActivity를 실제로 이동하고자 하는 액티비티로 변경
            startActivity(intent)
            finish() // 스플래시 액티비티를 종료하여 뒤로가기 버튼을 누를 때 다시 이 액티비티로 돌아오지 않도록 함
        }, 5000) // 5000ms = 5초
    }
}
