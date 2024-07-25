package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProposalFirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal_first)

        val overlayButton: Button = findViewById(R.id.overlay_button)
        overlayButton.setOnClickListener {
            val intent = Intent(this, ProposalActivity::class.java)
            startActivity(intent)
        }
    }

}