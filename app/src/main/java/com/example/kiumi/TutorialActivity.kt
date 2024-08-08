package com.example.kiumi

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button


class TutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial)

        // 버튼에 애니메이션 설정
        val orderButton: Button = findViewById(R.id.button_order)
        orderButton.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = orderButton.background as AnimationDrawable
        animationDrawable.start()


        // 주문하기 버튼 클릭 시
        findViewById<Button>(R.id.button_order).setOnClickListener {
            val intent = Intent(
                this@TutorialActivity,
                TutorialPlaceSelectionActivity::class.java
            )
            startActivity(intent)
        }
    }
}
