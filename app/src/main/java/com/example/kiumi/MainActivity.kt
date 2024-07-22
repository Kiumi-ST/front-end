package com.example.kiumi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.example.kiumi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var isTTSActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("com.example.kiumi.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        isTTSActive = sharedPref.getBoolean("isTTSActive", false)

        // Initialize toggle button state
        val toggleButton = findViewById<ToggleButton>(R.id.toggleButton)
        toggleButton.isChecked = isTTSActive

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

        toolbarLogo?.setOnClickListener {
        }

        findViewById<ImageView>(R.id.centerLogo).setOnClickListener {
            startActivity(Intent(this, ActualPracticeQRSuccess::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnPractice).setOnClickListener {
            startActivity(Intent(this, PracticeSelectionActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnHelp).setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive)
            startActivity(intent)
        }

        findViewById<RelativeLayout>(R.id.btnSurvey).setOnClickListener {
            startActivity(Intent(this, SurveyActivity::class.java))
        }

        toggleButton.setOnClickListener {
            isTTSActive = (it as ToggleButton).isChecked
            with(sharedPref.edit()) {
                putBoolean("isTTSActive", isTTSActive)
                apply()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        // Reset SharedPreferences when the app is restarted
        val sharedPref = getSharedPreferences("com.example.kiumi.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isTTSActive", false)
            apply()
        }

        // Reset toggle button state
        val toggleButton = findViewById<ToggleButton>(R.id.toggleButton)
        toggleButton.isChecked = false
        isTTSActive = false
    }
}
