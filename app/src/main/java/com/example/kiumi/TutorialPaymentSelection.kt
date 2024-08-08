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
            val intent = Intent(this, ActualPracticePayment::class.java)
            startActivity(intent)
        }

        // 모바일 상품권에 클릭 리스너 추가
        val giftCardLayout: LinearLayout = findViewById(R.id.linearLayoutGiftCard)
        giftCardLayout.setOnClickListener {
            // 이전 단계 액티비티로 이동 (변경 필요)
            // val intent = Intent(this, PreviousActivity::class.java)
            // startActivity(intent)
            // 현재는 단순히 토스트 메시지로 대체
            Toast.makeText(this, "모바일 상품권으로 이동", Toast.LENGTH_SHORT).show()
        }

        // 이전 단계 버튼에 클릭 리스너 추가
        val buttonPreviousStep: Button = findViewById(R.id.buttonPreviousStep)
        buttonPreviousStep.setOnClickListener {
            // 이전 단계 액티비티로 이동 (변경 필요)
            // val intent = Intent(this, PreviousActivity::class.java)
            // startActivity(intent)
            // 현재는 단순히 토스트 메시지로 대체
            Toast.makeText(this, "이전 단계로 이동", Toast.LENGTH_SHORT).show()
        }

        // 처음으로 버튼에 클릭 리스너 추가
        val buttonHome: Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            // 처음 화면으로 이동 (변경 필요)
            // val intent = Intent(this, HomeActivity::class.java)
            // startActivity(intent)
            // 현재는 단순히 토스트 메시지로 대체
            Toast.makeText(this, "처음 화면으로 이동", Toast.LENGTH_SHORT).show()
        }

        // 도움 기능 버튼에 클릭 리스너 추가
        val buttonHelp: LinearLayout = findViewById(R.id.linearLayoutHelp)
        buttonHelp.setOnClickListener {
            // 도움 기능 액티비티로 이동 (변경 필요)
            // val intent = Intent(this, HelpActivity::class.java)
            // startActivity(intent)
            // 현재는 단순히 토스트 메시지로 대체
            Toast.makeText(this, "도움 기능으로 이동", Toast.LENGTH_SHORT).show()
        }

        // LinearLayout에 애니메이션 설정
        val takeOutLayout: LinearLayout = findViewById(R.id.linearLayoutCardPayment)
        takeOutLayout.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = takeOutLayout.background as AnimationDrawable
        animationDrawable.start()

    }

}
