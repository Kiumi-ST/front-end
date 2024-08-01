package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ProposalFirstActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal_first)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        val overlayButton: Button = findViewById(R.id.overlay_button)
        overlayButton.setOnClickListener {
            val intent = Intent(this, ProposalActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        startTime = System.currentTimeMillis()
    }

    override fun onStop() {
        super.onStop()
        endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        val params = Bundle().apply {
            putLong("screen_duration", duration)
            putString("screen_name", "개선안_대기 팝업창")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

}