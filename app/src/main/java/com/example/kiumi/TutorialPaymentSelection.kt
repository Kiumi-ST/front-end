package com.example.kiumi

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TutorialPaymentSelection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_payment_selection)

        // 카드 결제에 클릭 리스너 추가
        val cardPaymentLayout: LinearLayout = findViewById(R.id.linearLayoutCardPayment)
        cardPaymentLayout.setOnClickListener {
            // ActualPracticePayment 액티비티로 이동
            val intent = Intent(this, ActualPracticePaymentCard::class.java)
            startActivity(intent)
        }

        // 모바일 상품권에 클릭 리스너 추가
        val giftCardLayout: LinearLayout = findViewById(R.id.linearLayoutGiftCard)
        giftCardLayout.setOnClickListener {

        }

        // 이전 단계 버튼에 클릭 리스너 추가
        val buttonPreviousStep: Button = findViewById(R.id.buttonPreviousStep)
        buttonPreviousStep.setOnClickListener {
            val intent = Intent(this, TutorialMainActivity::class.java)
            startActivity(intent)
        }

        // 처음으로 버튼에 클릭 리스너 추가
        val buttonHome: Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
        }

        // 도움 기능 버튼에 클릭 리스너 추가
        val buttonHelp: LinearLayout = findViewById(R.id.linearLayoutHelp)
        buttonHelp.setOnClickListener {
        }

        // LinearLayout에 애니메이션 설정
        val takeOutLayout: LinearLayout = findViewById(R.id.linearLayoutCardPayment)
        takeOutLayout.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = takeOutLayout.background as AnimationDrawable
        animationDrawable.start()

    }

}
