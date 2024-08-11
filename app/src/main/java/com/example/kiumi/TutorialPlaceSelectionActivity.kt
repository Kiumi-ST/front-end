package com.example.kiumi

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TutorialPlaceSelectionActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial_place_selection)

        // SharedPreferences 초기화 및 burger_set_clicked 값 초기화
        preferences = getSharedPreferences("com.example.kiumi.PREFERENCES", MODE_PRIVATE)
        preferences.edit().putBoolean("burger_set_clicked", false).apply()

        // LinearLayout에 애니메이션 설정
        val takeOutLayout: LinearLayout = findViewById(R.id.button_take_out)
        takeOutLayout.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = takeOutLayout.background as AnimationDrawable
        animationDrawable.start()

        // 포장 버튼 클릭 시
        takeOutLayout.setOnClickListener {
            startActivity(Intent(this, TutorialMainActivity::class.java))
        }
    }
}
