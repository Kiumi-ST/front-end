package com.example.kiumi

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.kiumi.ActualPracticePickup
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ActualPracticePayment : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_payment)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        Toast.makeText(this, "5초 동안 화면이 유지됩니다", Toast.LENGTH_LONG).show()

        val instructionsTextView = findViewById<TextView>(R.id.textViewInstructions)
        instructionsTextView.text = Html.fromHtml(getString(R.string.instructions_text), Html.FROM_HTML_MODE_LEGACY)

        // 5초 후에 다음 액티비티로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ActualPracticePickup::class.java)
            startActivity(intent)
            finish()
        }, 5000) // 5000ms = 5초

        // 첫 번째 ImageView의 애니메이션 설정 (아래로 먼저 이동 및 회전)
        val imageViewCard1 = findViewById<ImageView>(R.id.imageViewCard1)

        val translationX1 = 100f // 필요에 따라 조정
        val translationY1 = 100f // 필요에 따라 조정

        val animator1 = ObjectAnimator.ofPropertyValuesHolder(
            imageViewCard1,
            PropertyValuesHolder.ofFloat("translationX", 0f, translationX1),
            PropertyValuesHolder.ofFloat("translationY", 0f, translationY1),
            PropertyValuesHolder.ofFloat("rotation", 0f, -4f) // -4도 회전
        )

        animator1.duration = 1000 // 애니메이션 지속 시간 (밀리초 단위)
        animator1.interpolator = LinearInterpolator() // 애니메이션을 부드럽게
        animator1.repeatCount = ObjectAnimator.INFINITE // 무한 반복
        animator1.repeatMode = ObjectAnimator.REVERSE // 역방향 애니메이션

        animator1.start()

        // 두 번째 ImageView의 애니메이션 설정 (위로 먼저 이동, -45도 방향)
        val imageViewCard2 = findViewById<ImageView>(R.id.imageViewCard2)

        val translationX2 = 100f // 필요에 따라 조정
        val translationY2 = -100f // 필요에 따라 조정

        val animator2 = ObjectAnimator.ofPropertyValuesHolder(
            imageViewCard2,
            PropertyValuesHolder.ofFloat("translationX", 0f, translationX2),
            PropertyValuesHolder.ofFloat("translationY", 0f, translationY2)
        )

        animator2.duration = 1000 // 애니메이션 지속 시간 (밀리초 단위)
        animator2.interpolator = LinearInterpolator() // 애니메이션을 부드럽게
        animator2.repeatCount = ObjectAnimator.INFINITE // 무한 반복
        animator2.repeatMode = ObjectAnimator.REVERSE // 역방향 애니메이션

        animator2.start()

        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    // 뒤로 가기 버튼을 눌렀을 때 실행되는 콜백 메소드
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            val params = Bundle().apply {
                putString("previous_screen_name", previousActivity)
                putString("screen_name", "실전 연습_카드 결제")
            }
            firebaseAnalytics.logEvent("go_back", params)

            // 실제로 뒤로 가기 동작을 수행하도록 추가
            isEnabled = false // 콜백을 비활성화하여 기본 뒤로 가기 동작을 수행
            onBackPressedDispatcher.onBackPressed() // 기본 뒤로 가기 동작 수행
        }
    }

}
