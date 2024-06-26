package com.example.kiumi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout

class PracticeSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_selection)

        findViewById<RelativeLayout>(R.id.btnTutorial).setOnClickListener {
            startActivity(Intent(this, TutorialActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnActualPractice).setOnClickListener {
            startActivity(Intent(this, ActualPracticeActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnProposal).setOnClickListener {
            startActivity(Intent(this, ProposalActivity::class.java))
        }
    }
}