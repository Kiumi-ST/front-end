package com.example.kiumi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.example.kiumi.databinding.ActivityMainBinding
import com.example.kiumi.ActualPracticeQRSuccess

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화

        val toolbarView = supportActionBar?.customView
        val homeIcon = toolbarView?.findViewById<ImageView>(R.id.home_icon)
        val titleView = toolbarView?.findViewById<TextView>(R.id.toolbar_title)

        titleView?.text = "키오스크 배우미"

        homeIcon?.setOnClickListener {
            // 홈 아이콘 클릭 시 동작 구현
        }




        findViewById<RelativeLayout>(R.id.btnPractice).setOnClickListener {
            startActivity(Intent(this, PracticeSelectionActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnHelp).setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnSurvey).setOnClickListener {
            startActivity(Intent(this, SurveyActivity::class.java))
        }

        // 여기에 추가된 코드
        findViewById<ToggleButton>(R.id.toggleButton).setOnClickListener {
            startActivity(Intent(this, ActualPracticeQRSuccess::class.java))
        }
    }
}