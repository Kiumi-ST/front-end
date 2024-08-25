package com.example.kiumi

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class TutorialPickup : PopupActivity() {

    private var isTTSActive: Boolean = false

    // Handler와 Runnable을 클래스 변수로 정의
    private val handler = Handler(Looper.getMainLooper())
    private val navigateRunnable = Runnable {
        val intent = Intent(this, TutorialThankYou::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_pickup)


        // 이전 액티비티에서 전달된 isTTSActive 값 받아오기 (기본값 false)
        isTTSActive = intent.getBooleanExtra("isTTSActive", false)

        // 2.5초 후에 다음 액티비티로 이동
        handler.postDelayed(navigateRunnable, 2500)

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
        val handlerShake = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                shakeAnimator1.start()
                handlerShake.postDelayed({
                    shakeAnimator2.start()
                }, 600) // 두 애니메이션 사이의 시간 간격
                handlerShake.postDelayed(this, 1000) // 전체 주기의 시간 간격
            }
        }

        handlerShake.post(runnable)
    }

}
