package com.example.kiumi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.kiumi.databinding.ActivitySurveyBinding
import kotlinx.coroutines.launch
import java.util.*

class SurveyActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    lateinit var binding: ActivitySurveyBinding
    private lateinit var ratingBar: RatingBar
    private lateinit var radioGroupQ2: RadioGroup
    private lateinit var radioGroupQ3: RadioGroup
    private lateinit var radioGroupQ4: RadioGroup
    private lateinit var editTextQ5: EditText
    private lateinit var btnFinish: Button
    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve isTTSActive value from SharedPreferences
        val sharedPref = getSharedPreferences("com.example.kiumi.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        isTTSActive = sharedPref.getBoolean("isTTSActive", false)

        // Initialize TextToSpeech
        tts = TextToSpeech(this, this)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button

        val toolbarView = supportActionBar?.customView
        val homeIcon = toolbarView?.findViewById<ImageView>(R.id.home_icon)
        val titleView = toolbarView?.findViewById<TextView>(R.id.toolbar_title)

        titleView?.text = "개선점 설문조사"

        homeIcon?.setOnClickListener {
            // 홈 아이콘 클릭 시 동작 구현
        }

        ratingBar = findViewById(R.id.ratingBar)
        radioGroupQ2 = findViewById(R.id.radioGroupQ2)
        radioGroupQ3 = findViewById(R.id.radioGroupQ3)
        radioGroupQ4 = findViewById(R.id.radioGroupQ4)
        editTextQ5 = findViewById(R.id.editTextTextMultiLine)
        btnFinish = findViewById(R.id.btnfinish)

        btnFinish.setOnClickListener {
            submitSurvey()
        }

        setupTTSButtons()
    }

    private fun setupTTSButtons() {
        val toggleButtons = mapOf(
            R.id.toggleAnswerButton1 to R.id.toggleAnswerButton1_text,
            R.id.toggleAnswerButton2 to R.id.toggleAnswerButton2_text,
            R.id.toggleAnswerButton3 to R.id.toggleAnswerButton3_text,
            R.id.toggleAnswerButton4 to R.id.toggleAnswerButton4_text,
            R.id.toggleAnswerButton5 to R.id.toggleAnswerButton5_text
        )

        for ((buttonId, textId) in toggleButtons) {
            findViewById<ImageView>(buttonId).setOnClickListener {
                if (isTTSActive) {
                    val text = findViewById<TextView>(textId).text.toString()
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
    }

    private fun submitSurvey() {
        val q1 = ratingBar.rating
        val q2 = findViewById<RadioButton>(radioGroupQ2.checkedRadioButtonId)?.text.toString()
        val q3 = findViewById<RadioButton>(radioGroupQ3.checkedRadioButtonId)?.text.toString()
        val q4 = findViewById<RadioButton>(radioGroupQ4.checkedRadioButtonId)?.text.toString()
        val q5 = editTextQ5.text.toString()

        val surveyData = SurveyData(q1, q2, q3, q4, q5)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.springBootApiService.submitSurvey(surveyData)
                if (response.isSuccessful) {
                    val surveyResponse = response.body()
                    Toast.makeText(this@SurveyActivity, surveyResponse?.msg, Toast.LENGTH_LONG).show()
                    val intent = Intent(
                        this@SurveyActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@SurveyActivity,
                        "제출 실패: ${response.errorBody()?.string()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SurveyActivity, "오류 발생: ${e.message}", Toast.LENGTH_LONG).show()
                Log.d("mobile","오류 발생: ${e.message}")
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle the error
            }
        }
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
