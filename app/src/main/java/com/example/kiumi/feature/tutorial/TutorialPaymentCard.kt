package com.example.kiumi.feature.tutorial

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
import com.example.kiumi.feature.common.PopupActivity
import com.example.kiumi.R

class TutorialPaymentCard : PopupActivity() {

    private var isTTSActive: Boolean = false

    // Handler와 Runnable을 클래스 변수로 정의
    private val handler = Handler(Looper.getMainLooper())
    private val navigateRunnable = Runnable {
        val intent = Intent(this, TutorialPickup::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_payment_card)

        // 이전 액티비티에서 전달된 isTTSActive 값 받아오기
        isTTSActive = intent.getBooleanExtra("isTTSActive", false)

        // 5초 후에 다음 액티비티로 이동
        handler.postDelayed(navigateRunnable, 5000)

        val instructionsTextView = findViewById<TextView>(R.id.textViewInstructions)
        instructionsTextView.text = Html.fromHtml(getString(R.string.instructions_text), Html.FROM_HTML_MODE_LEGACY)

        Toast.makeText(this, "5초 동안 화면이 유지됩니다", Toast.LENGTH_LONG).show()

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
    }
}
