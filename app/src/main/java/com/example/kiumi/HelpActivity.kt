package com.example.kiumi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.kiumi.databinding.ActivityHelpBinding
import java.util.*

class HelpActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityHelpBinding
    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TextToSpeech(this, this)

        val sharedPref = getSharedPreferences("com.example.kiumi.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        isTTSActive = sharedPref.getBoolean("isTTSActive", false)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.toolbar)

        val toolbarView = supportActionBar?.customView
        val homeIcon = toolbarView?.findViewById<ImageView>(R.id.home_icon)
        val titleView = toolbarView?.findViewById<TextView>(R.id.toolbar_title)
        val backIcon = toolbarView?.findViewById<ImageView>(R.id.back_icon)

        backIcon?.setOnClickListener {
            finish()
        }

        titleView?.text = "도움말"

        homeIcon?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        setupToggleButtons()

        findViewById<Button>(R.id.btnEngPractice).setOnClickListener {
            startActivity(Intent(this, EnglishPracticeActivity::class.java))
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 언어가 지원되지 않음
            }
        }
    }

    private fun setupToggleButtons() {
        setupToggleButton(R.id.toggleAnswerButton1, R.id.answerDivider1, R.id.answerTextView1)
        setupToggleButton(R.id.toggleAnswerButton2, R.id.answerDivider2, R.id.answerTextView2)
        setupToggleButton(R.id.toggleAnswerButton3, R.id.answerDivider3, R.id.answerTextView3)
        setupToggleButton(R.id.toggleAnswerButton4, R.id.answerDivider4, R.id.answerTextView4)
        setupToggleButton(R.id.toggleAnswerButton5, R.id.answerDivider5, R.id.answerTextView5)
    }

    private fun setupToggleButton(toggleButtonId: Int, dividerId: Int, textViewId: Int) {
        val toggleButton = findViewById<ImageView>(toggleButtonId)
        val divider = findViewById<View>(dividerId)
        val textView = findViewById<TextView>(textViewId)

        toggleButton.setOnClickListener {
            val isAnswerVisible = textView.visibility == View.VISIBLE
            divider.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE
            textView.visibility = if (isAnswerVisible) View.GONE else View.VISIBLE
            toggleButton.setImageResource(
                if (isAnswerVisible) R.drawable.plus_button else R.drawable.minus_button
            )
            if (!isAnswerVisible && isTTSActive) {
                speakTextWithDelay(textView.text.toString(), 500)
            } else {
                stopTTS()
            }
        }
    }

    private fun speakTextWithDelay(text: String, delayMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }, delayMillis)
    }

    private fun stopTTS() {
        if (tts.isSpeaking) {
            tts.stop()
        }
    }

    override fun onPause() {
        super.onPause()
        stopTTS()
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
