package com.example.kiumi

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TutorialSideMenuSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial_side_menu_selection)

        findViewById<LinearLayout>(R.id.button_side1).setOnClickListener {
            val intent = Intent(this, TutorialDrinkSelectionActiviy::class.java)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.button_side2).setOnClickListener {
        }

        findViewById<Button>(R.id.button_back).setOnClickListener {
            val intent = Intent(this, TutorialSetSelectionActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, TutorialMainActivity::class.java)
            startActivity(intent)
        }

        // LinearLayout에 애니메이션 설정
        val takeOutLayout: LinearLayout = findViewById(R.id.button_side1)
        takeOutLayout.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = takeOutLayout.background as AnimationDrawable
        animationDrawable.start()
    }
}