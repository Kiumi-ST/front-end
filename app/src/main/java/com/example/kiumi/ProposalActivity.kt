package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ProposalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal)

        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            val intent = Intent(
                this@ProposalActivity,
                ActualPracticeQRSuccess::class.java
            )
            startActivity(intent)
        }

        // 주문하기 버튼 클릭 시
        findViewById<Button>(R.id.button_order).setOnClickListener {
            val intent = Intent(
                this@ProposalActivity,
                ProposalPlaceSelection::class.java
            )
            startActivity(intent)
        }

        // 언어 버튼 클릭 시
        findViewById<Button>(R.id.button_language).setOnClickListener {
        }

        // 도움 버튼 클릭 시
        findViewById<Button>(R.id.button_help).setOnClickListener {
        }
    }

}