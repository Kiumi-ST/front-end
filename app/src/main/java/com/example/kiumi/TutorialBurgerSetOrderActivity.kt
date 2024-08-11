package com.example.kiumi

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TutorialBurgerSetOrderActivity : AppCompatActivity() {

    private var quantity = 1
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial_burger_set_order)

        preferences = getSharedPreferences("com.example.kiumi.PREFERENCES", MODE_PRIVATE)

        val quantityText: TextView = findViewById(R.id.quantity)

        findViewById<Button>(R.id.button_add_to_cart).setOnClickListener {
            // burger_set_clicked 상태를 true로 저장
            preferences.edit().putBoolean("burger_set_clicked", true).apply()

            // TutorialMainActivity로 이동
            val intent = Intent(this, TutorialMainActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, TutorialMainActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_decrease_quantity).setOnClickListener {
        }

        findViewById<Button>(R.id.button_increase_quantity).setOnClickListener {
        }

        // Button에 애니메이션 설정
        val addToCartButton: Button = findViewById(R.id.button_add_to_cart)
        addToCartButton.setBackgroundResource(R.drawable.blinking_border_animation_yellow)
        val animationDrawable = addToCartButton.background as AnimationDrawable
        animationDrawable.start()
    }
}
