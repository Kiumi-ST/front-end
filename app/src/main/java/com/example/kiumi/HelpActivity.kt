package com.example.kiumi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.example.kiumi.databinding.ActivityHelpBinding
import com.example.kiumi.databinding.ActivityMainBinding

class HelpActivity : AppCompatActivity() {
    lateinit var binding: ActivityHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //툴바
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화

        val toolbarView = supportActionBar?.customView
        val homeIcon = toolbarView?.findViewById<ImageView>(R.id.home_icon)
        val titleView = toolbarView?.findViewById<TextView>(R.id.toolbar_title)

        titleView?.text = "도움말"

        homeIcon?.setOnClickListener {
            // 홈 아이콘 클릭 시 동작 구현
        }


    }
}