package com.example.kiumi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.example.kiumi.databinding.ActivityActualPracticeMainBinding
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

class PracticeSelectionActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_selection)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        //툴바
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화

        val toolbarView = supportActionBar?.customView
        val homeIcon = toolbarView?.findViewById<ImageView>(R.id.home_icon)
        val titleView = toolbarView?.findViewById<TextView>(R.id.toolbar_title)

        titleView?.text = "연습 방식 선택"

        homeIcon?.setOnClickListener {
            // 홈 아이콘 클릭 시 동작 구현
        }

        findViewById<RelativeLayout>(R.id.btnTutorial).setOnClickListener {
            firebaseAnalytics.logEvent("select_practice_option"){
                param(FirebaseAnalytics.Param.CONTENT, "tutorial")
            }
            startActivity(Intent(this, TutorialActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnActualPractice).setOnClickListener {
            firebaseAnalytics.logEvent("select_practice_option"){
//                param("tts_checked", isTTSActive.toString())
                param(FirebaseAnalytics.Param.CONTENT, "actual_practice")
            }
            startActivity(Intent(this, ActualPracticeActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnProposal).setOnClickListener {
            firebaseAnalytics.logEvent("select_practice_option"){
//                param("tts_checked", isTTSActive.toString())
                param(FirebaseAnalytics.Param.CONTENT, "proposal")
            }
            startActivity(Intent(this, ProposalFirstActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        // 서비스 중지
        val serviceIntent = Intent(this, PhotoCaptureService::class.java)
        stopService(serviceIntent)
    }
}