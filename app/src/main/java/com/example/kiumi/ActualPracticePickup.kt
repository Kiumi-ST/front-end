package com.example.kiumi

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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

        // 첫 번째 ImageView의 애니메이션 설정
        val imageViewPickup1 = findViewById<ImageView>(R.id.imageViewPickup1)
        val imageViewPickup2 = findViewById<ImageView>(R.id.imageViewPickup2)

        // 흔들리는 애니메이션 설정
        val shakeAnimator1 = ObjectAnimator.ofPropertyValuesHolder(
            imageViewPickup1,
            PropertyValuesHolder.ofFloat("translationX", 0f, 10f, -10f, 0f)
        ).apply {
            duration = 200 // 애니메이션 지속 시간 (밀리초 단위)
            interpolator = LinearInterpolator()
        }

        val shakeAnimator2 = ObjectAnimator.ofPropertyValuesHolder(
            imageViewPickup2,
            PropertyValuesHolder.ofFloat("translationX", 0f, 10f, -10f, 0f)
        ).apply {
            duration = 200 // 애니메이션 지속 시간 (밀리초 단위)
            interpolator = LinearInterpolator()
        }

        // 교대로 흔들리게 하는 핸들러 설정
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                shakeAnimator1.start()
                handler.postDelayed({
                    shakeAnimator2.start()
                }, 600) // 두 애니메이션 사이의 시간 간격
                handler.postDelayed(this, 1000) // 전체 주기의 시간 간격
            }
        }

        handler.post(runnable)
    }
}
