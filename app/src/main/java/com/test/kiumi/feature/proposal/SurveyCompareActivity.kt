package com.test.kiumi.feature.proposal

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
import com.test.kiumi.R
import com.test.kiumi.data.RetrofitClient
import com.test.kiumi.data.VotingRequest
import com.test.kiumi.databinding.ActivitySurveyCompareBinding
import com.test.kiumi.feature.home.MainActivity
import kotlinx.coroutines.launch
import java.util.*

class SurveyCompareActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    lateinit var binding: ActivitySurveyCompareBinding

    private lateinit var question1Group: RadioGroup
    private lateinit var question2Group: RadioGroup
    private lateinit var commentsEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyCompareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve isTTSActive value from SharedPreferences
        val sharedPref = getSharedPreferences("com.example.kiumi.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        isTTSActive = sharedPref.getBoolean("isTTSActive", false)
        Log.d("TTS_DEBUG", "TTS 활성화 상태: $isTTSActive")

        // Initialize TextToSpeech
        tts = TextToSpeech(this, this)

        //툴바
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

        titleView?.text = "설문조사"

        homeIcon?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        question1Group = findViewById(R.id.check)
        question2Group = findViewById(R.id.comf)
        commentsEditText = findViewById(R.id.editTextTextMultiLine)
        submitButton = findViewById(R.id.btnfinish)

        submitButton.setOnClickListener {
            sendVotingData()
        }

        setupTTSButtons()
    }

    private fun setupTTSButtons() {
        val textToReadMap = mapOf(
            R.id.toggleAnswerButton1 to R.id.question1_text,
            R.id.toggleAnswerButton2 to R.id.question2_text,
            R.id.toggleAnswerButton3 to R.id.question3_text
        )

        for ((buttonId, textId) in textToReadMap) {
            findViewById<ImageView>(buttonId).setOnClickListener {
                if (isTTSActive) {
                    val textView = findViewById<TextView>(textId)
                    val text = textView.text.toString().also {
                        Log.d("TTS_DEBUG", "TextView 텍스트: $it")
                    }

                    if (text.isNotEmpty()) {
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                        Log.d("TTS_DEBUG", "TTS로 텍스트 읽기 시작")
                    } else {
                        Log.d("TTS_DEBUG", "읽을 텍스트가 없음")
                    }
                } else {
                    Log.d("TTS_DEBUG", "TTS가 비활성화되어 있음")
                }
            }
        }
    }


    private fun sendVotingData() {
        val question1Answer = findViewById<RadioButton>(question1Group.checkedRadioButtonId)?.text.toString()
        val question2Answer = findViewById<RadioButton>(question2Group.checkedRadioButtonId)?.text.toString()
        val comments = commentsEditText.text.toString()

        val votingRequest = VotingRequest(question1Answer, question2Answer, comments)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.springBootApiService.submitVotingResult(votingRequest)
                if (response.isSuccessful) {
                    val votingResponse = response.body()
                    Toast.makeText(this@SurveyCompareActivity, votingResponse?.msg, Toast.LENGTH_LONG).show()
                    val intent = Intent(
                        this@SurveyCompareActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@SurveyCompareActivity,
                        "제출 실패: ${response.errorBody()?.string()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SurveyCompareActivity, "오류 발생: ${e.message}", Toast.LENGTH_LONG).show()
                Log.d("mobile", "오류 발생: ${e.message}")
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS_DEBUG", "언어 지원되지 않음 또는 데이터 누락")
            } else {
                Log.d("TTS_DEBUG", "TTS 초기화 성공")
            }
        } else {
            Log.e("TTS_DEBUG", "TTS 초기화 실패")
        }
    }

    private fun stopTTS() {
        if (tts.isSpeaking) {
            Log.d("TTS_DEBUG", "TTS 중지")
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
            Log.d("TTS_DEBUG", "TTS 종료")
        }
        super.onDestroy()
    }
}
