package com.example.kiumi

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TutorialDrinkSelectionActiviy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial_drink_selection_activiy)

        findViewById<LinearLayout>(R.id.button_set).setOnClickListener {
            val intent = Intent(this, TutorialBurgerSetOrderActivity::class.java)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.button_large_set).setOnClickListener {
            val intent = Intent(this, TutorialBurgerSetOrderActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, TutorialMainActivity::class.java)
            startActivity(intent)
        }

        // LinearLayout에 애니메이션 설정
        val takeOutLayout: LinearLayout = findViewById(R.id.button_set)
        takeOutLayout.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = takeOutLayout.background as AnimationDrawable
        animationDrawable.start()
    }
}