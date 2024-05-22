package com.example.kiumi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        val toggleButton1 = findViewById<ImageView>(R.id.toggleAnswerButton1)
        toggleButton1.setOnClickListener {
            val answerDivider = findViewById<View>(R.id.answerDivider1)
            val answerTextView = findViewById<TextView>(R.id.answerTextView1)
            val isAnswerVisible = answerTextView.visibility == View.VISIBLE

            answerDivider.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE
            answerTextView.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE

            val toggleButton = findViewById<ImageView>(R.id.toggleAnswerButton1)
            toggleButton.setImageResource(
                if (isAnswerVisible) R.drawable.plus_button
                else R.drawable.minus_button // minus_button 리소스를 준비해야 합니다.
            )
        }

        val toggleButton2 = findViewById<ImageView>(R.id.toggleAnswerButton2)
        toggleButton2.setOnClickListener {
            val answerDivider = findViewById<View>(R.id.answerDivider2)
            val answerTextView = findViewById<TextView>(R.id.answerTextView2)
            val isAnswerVisible = answerTextView.visibility == View.VISIBLE

            answerDivider.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE
            answerTextView.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE

            val toggleButton = findViewById<ImageView>(R.id.toggleAnswerButton2)
            toggleButton.setImageResource(
                if (isAnswerVisible) R.drawable.plus_button
                else R.drawable.minus_button // minus_button 리소스를 준비해야 합니다.
            )
        }

        val toggleButton3 = findViewById<ImageView>(R.id.toggleAnswerButton3)
        toggleButton3.setOnClickListener {
            val answerDivider = findViewById<View>(R.id.answerDivider3)
            val answerTextView = findViewById<TextView>(R.id.answerTextView3)
            val isAnswerVisible = answerTextView.visibility == View.VISIBLE

            answerDivider.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE
            answerTextView.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE

            val toggleButton = findViewById<ImageView>(R.id.toggleAnswerButton3)
            toggleButton.setImageResource(
                if (isAnswerVisible) R.drawable.plus_button
                else R.drawable.minus_button // minus_button 리소스를 준비해야 합니다.
            )
        }

        val toggleButton4 = findViewById<ImageView>(R.id.toggleAnswerButton4)
        toggleButton4.setOnClickListener {
            val answerDivider = findViewById<View>(R.id.answerDivider4)
            val answerTextView = findViewById<TextView>(R.id.answerTextView4)
            val isAnswerVisible = answerTextView.visibility == View.VISIBLE

            answerDivider.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE
            answerTextView.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE

            val toggleButton = findViewById<ImageView>(R.id.toggleAnswerButton4)
            toggleButton.setImageResource(
                if (isAnswerVisible) R.drawable.plus_button
                else R.drawable.minus_button // minus_button 리소스를 준비해야 합니다.
            )
        }

        val toggleButton5 = findViewById<ImageView>(R.id.toggleAnswerButton5)
        toggleButton5.setOnClickListener {
            val answerDivider = findViewById<View>(R.id.answerDivider5)
            val answerTextView = findViewById<TextView>(R.id.answerTextView5)
            val isAnswerVisible = answerTextView.visibility == View.VISIBLE

            answerDivider.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE
            answerTextView.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE

            val toggleButton = findViewById<ImageView>(R.id.toggleAnswerButton5)
            toggleButton.setImageResource(
                if (isAnswerVisible) R.drawable.plus_button
                else R.drawable.minus_button // minus_button 리소스를 준비해야 합니다.
            )
        }

    }
}