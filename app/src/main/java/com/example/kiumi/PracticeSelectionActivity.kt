package com.example.kiumi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import java.util.*


class PracticeSelectionActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var isTTSActive: Boolean = false
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_selection)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // SharedPreferences에서 TTS 상태 불러오기
        val sharedPref = getSharedPreferences("com.example.kiumi.PREFERENCE_FILE_KEY", MODE_PRIVATE)
        isTTSActive = sharedPref.getBoolean("isTTSActive", false)

        // TTS 초기화
        tts = TextToSpeech(this, this)

        // 툴바 설정
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
            firebaseAnalytics.logEvent("select_practice_option") {
                param(FirebaseAnalytics.Param.CONTENT, "tutorial")
            }
            startActivity(Intent(this, TutorialActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.btnActualPractice).setOnClickListener {
            firebaseAnalytics.logEvent("select_practice_option") {
                param(FirebaseAnalytics.Param.CONTENT, "actual_practice")
            }
            val intent = Intent(this, ActualPracticeActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive)
            startActivity(intent)
        }

        findViewById<RelativeLayout>(R.id.btnProposal).setOnClickListener {
            firebaseAnalytics.logEvent("select_practice_option") {
                param(FirebaseAnalytics.Param.CONTENT, "proposal")
            }
            val intent = Intent(this, ProposalFirstActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive)
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        // 서비스 중지
        val serviceIntent = Intent(this, PhotoCaptureService::class.java)
        stopService(serviceIntent)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 언어가 지원되지 않음
            } else if (isTTSActive) {
                // TTS가 활성화된 경우 음성 출력
                //speakText("순서지침 버튼을 눌러주세요")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 액티비티가 다시 포커스를 얻을 때 TTS 활성화 상태에 따라 음성 출력
        if (isTTSActive && ::tts.isInitialized) {
            //speakText("순서지침 버튼을 눌러주세요")
        }
    }

    private fun speakText(text: String) {
        if (isTTSActive) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onPause() {
        super.onPause()
        // 다른 액티비티로 넘어갈 때 TTS 멈춤
        if (::tts.isInitialized && tts.isSpeaking) {
            tts.stop()
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}