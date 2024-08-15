package com.example.kiumi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.example.kiumi.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent



class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var isTTSActive: Boolean = false
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences에서 TTS 상태 불러오기
        val sharedPref = getSharedPreferences("com.example.kiumi.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        isTTSActive = sharedPref.getBoolean("isTTSActive", false)

        val toggleButton = findViewById<ToggleButton>(R.id.toggleButton)
        toggleButton.isChecked = isTTSActive

        toggleButton.setOnClickListener {
            isTTSActive = (it as ToggleButton).isChecked
            with(sharedPref.edit()) {
                putBoolean("isTTSActive", isTTSActive)
                apply()
            }
        }

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Initialize toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.toolbar)

        val toolbarView = supportActionBar?.customView
        val homeIcon = toolbarView?.findViewById<ImageView>(R.id.home_icon)
        val titleView = toolbarView?.findViewById<TextView>(R.id.toolbar_title)
        val toolbarLogo = toolbarView?.findViewById<ImageView>(R.id.toolbar_logo)

        homeIcon?.setOnClickListener {
            // 홈 아이콘 클릭 시 동작 구현
        }

        toolbarLogo?.setOnClickListener {}

        findViewById<ImageView>(R.id.centerLogo).setOnClickListener {}

        findViewById<RelativeLayout>(R.id.btnPractice).setOnClickListener {
            firebaseAnalytics.logEvent("button_click_practice") {
                param(FirebaseAnalytics.Param.CONTENT, "practice")
            }
            val intent = Intent(this, PracticeSelectionActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive)  // TTS 상태 전달
            startActivity(intent)
        }

        findViewById<RelativeLayout>(R.id.btnHelp).setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive)  // TTS 상태 전달
            startActivity(intent)
        }

        findViewById<RelativeLayout>(R.id.btnSurvey).setOnClickListener {
            startActivity(Intent(this, SurveyActivity::class.java))
        }

        Log.d(MainActivity::class.simpleName, "패키지명: $packageName")
    }
}