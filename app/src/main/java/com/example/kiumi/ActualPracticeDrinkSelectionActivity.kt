package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ActualPracticeDrinkSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_drink_selection)

        findViewById<LinearLayout>(R.id.button_set).setOnClickListener {
            val intent = Intent(this, ActualPracticeBurgerSetOrderActivity::class.java)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.button_large_set).setOnClickListener {
            val intent = Intent(this, ActualPracticeBurgerSetOrderActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, ActualPracticeMainActivity::class.java)
            startActivity(intent)
        }
    }
}