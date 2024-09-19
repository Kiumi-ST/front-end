package com.example.kiumi.feature.tutorial

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kiumi.feature.common.PhotoCaptureService
import com.example.kiumi.R
import java.util.Locale;


class TutorialActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var isTTSActive: Boolean = false
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial)

        // SharedPreferences에서 TTS 상태 불러오기
        val sharedPref = getSharedPreferences("com.example.kiumi.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        isTTSActive = sharedPref.getBoolean("isTTSActive", false)

        // TTS 초기화
        tts = TextToSpeech(this, this)

        // 버튼에 애니메이션 설정
        val orderButton: Button = findViewById(R.id.button_order)
        orderButton.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = orderButton.background as AnimationDrawable
        animationDrawable.start()

        // 주문하기 버튼 클릭 시
        findViewById<Button>(R.id.button_order).setOnClickListener {
            val intent = Intent(
                this@TutorialActivity,
                TutorialPlaceSelectionActivity::class.java
            )
            // TTS 상태를 다음 액티비티로 전달
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
            } else {
                // TTS가 활성화된 경우 메시지 읽기
                if (isTTSActive) {
                    speakText("주문하기를 클릭해주세요")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 액티비티가 다시 포커스를 얻을 때 TTS 활성화 상태에 따라 음성 출력
        if (isTTSActive && ::tts.isInitialized) {
            speakText("주문하기를 클릭해주세요")
        }
    }

    private fun speakText(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
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
