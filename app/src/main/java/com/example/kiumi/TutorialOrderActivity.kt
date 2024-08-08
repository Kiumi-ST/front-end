package com.example.kiumi

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TutorialOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_order)

        // order_finish TextView 클릭 시 이벤트 설정
        findViewById<TextView>(R.id.order_finish).setOnClickListener {
            val intent = Intent(this, TutorialPaymentSelection::class.java)
            startActivity(intent)
        }

        // TextView에 애니메이션 설정
        val orderFinishTextView: TextView = findViewById(R.id.order_finish)
        orderFinishTextView.setBackgroundResource(R.drawable.blinking_border_animation_yellow)
        val animationDrawable = orderFinishTextView.background as AnimationDrawable
        animationDrawable.start()
    }
}
