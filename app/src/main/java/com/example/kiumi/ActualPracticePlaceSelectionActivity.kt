package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ActualPracticePlaceSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_place_selection)

        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            val intent = Intent(
                this@ActualPracticePlaceSelectionActivity,
                ActualPracticeQRSuccess::class.java
            )
            startActivity(intent)
        }

        // 매장 식사 버튼 클릭 시
        findViewById<LinearLayout>(R.id.button_dine_in).setOnClickListener {
        }

        // 포장 버튼 클릭 시
        findViewById<LinearLayout>(R.id.button_take_out).setOnClickListener {
        }

        // 처음으로 버튼 클릭 시
        findViewById<Button>(R.id.buttonHome).setOnClickListener {
        }

        // 도움 기능 버튼 클릭 시
        findViewById<LinearLayout>(R.id.linearLayoutHelp).setOnClickListener {
        }

    }
}