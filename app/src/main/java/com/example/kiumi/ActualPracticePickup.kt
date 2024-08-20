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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ActualPracticePickup : PopupActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var previousActivity: String? = null

    // Handler와 Runnable을 클래스 변수로 정의
    private val handler = Handler(Looper.getMainLooper())
    private val navigateRunnable = Runnable {
        val intent = Intent(this, ActualPracticeThankYou::class.java).apply { putExtra("previous_activity", "실전 연습_결제 방법") }
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_pickup)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")


        // 5초 후에 다음 액티비티로 이동
        handler.postDelayed(navigateRunnable, 5000) // 5000ms = 5초

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

        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    // 뒤로 가기 버튼을 눌렀을 때 실행되는 콜백 메소드
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            val params = Bundle().apply {
                putString("previous_screen_name", previousActivity)
                putString("screen_name", "실전 연습_결제 완료")
            }
            firebaseAnalytics.logEvent("go_back", params)

            // 예약된 5초 후 이동 작업을 취소
            handler.removeCallbacks(navigateRunnable)

            // 실제로 뒤로 가기 동작을 수행하도록 추가
            isEnabled = false // 콜백을 비활성화하여 기본 뒤로 가기 동작을 수행
            onBackPressedDispatcher.onBackPressed() // 기본 뒤로 가기 동작 수행
        }
    }

    private fun notifyServiceOfCurrentActivity() {
        val serviceIntent = Intent(this, PhotoCaptureService::class.java)
        serviceIntent.putExtra("ACTIVITY_NAME", this::class.java.simpleName)
        // 이미 실행 중이면 onStartCommand만 호출됨
        startService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()
        notifyServiceOfCurrentActivity()
    }
}
