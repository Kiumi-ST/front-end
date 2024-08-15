package com.example.kiumi

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class TutorialOrderActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false // 기본값을 false로 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_order)

        // 이전 액티비티에서 전달된 isTTSActive 값 받아오기 (기본값 false)
        isTTSActive = intent.getBooleanExtra("isTTSActive", false)

        // TTS 초기화
        tts = TextToSpeech(this, this)

        // order_finish TextView 클릭 시 이벤트 설정
        findViewById<TextView>(R.id.order_finish).setOnClickListener {
            val intent = Intent(this, TutorialPaymentSelection::class.java)
            // isTTSActive 값을 전달 (기본값 false로 설정)
            intent.putExtra("isTTSActive", isTTSActive)
            startActivity(intent)
        }

        // TextView에 애니메이션 설정
        val orderFinishTextView: TextView = findViewById(R.id.order_finish)
        orderFinishTextView.setBackgroundResource(R.drawable.blinking_border_animation_yellow)
        val animationDrawable = orderFinishTextView.background as AnimationDrawable
        animationDrawable.start()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 언어가 지원되지 않음
            } else if (isTTSActive) {
                // TTS가 활성화된 경우 음성 출력
                speakText("주문완료를 클릭해주세요")
            }
        }
    }

    private fun speakText(text: String) {
        if (isTTSActive) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onResume() {
        super.onResume()
        // 액티비티가 다시 포커스를 얻을 때 TTS 활성화 상태에 따라 음성 출력
        if (isTTSActive && ::tts.isInitialized) {
            speakText("주문완료를 클릭해주세요")
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
