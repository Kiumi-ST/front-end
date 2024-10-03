package com.test.kiumi.feature.tutorial

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.test.kiumi.R
import java.util.*

class TutorialDrinkSelectionActiviy : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false // 기본값을 false로 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial_drink_selection_activiy)

        // 이전 액티비티에서 전달된 isTTSActive 값 받아오기 (기본값 false)
        isTTSActive = intent.getBooleanExtra("isTTSActive", false)

        // TTS 초기화
        tts = TextToSpeech(this, this)

        findViewById<LinearLayout>(R.id.button_drink1).setOnClickListener {
            val intent = Intent(this, TutorialBurgerSetOrderActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive) // 다음 액티비티로 isTTSActive 값 전달
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.button_drink2).setOnClickListener {
            // 추가 기능을 구현할 수 있습니다.
        }

        findViewById<LinearLayout>(R.id.button_drink3).setOnClickListener {
            // 추가 기능을 구현할 수 있습니다.
        }

        findViewById<Button>(R.id.button_back).setOnClickListener {
            val intent = Intent(this, TutorialSideMenuSelectionActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive) // 이전 액티비티로 돌아갈 때도 isTTSActive 값 전달
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, TutorialMainActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive) // 메인으로 돌아갈 때도 isTTSActive 값 전달
            startActivity(intent)
        }

        // LinearLayout에 애니메이션 설정
        val takeOutLayout: LinearLayout = findViewById(R.id.button_drink1)
        takeOutLayout.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = takeOutLayout.background as AnimationDrawable
        animationDrawable.start()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 언어가 지원되지 않음
            } else if (isTTSActive) {
                // TTS가 활성화된 경우 음성 출력
                speakText("코카 콜라를 클릭해주세요")
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
            speakText("코카 콜라를 클릭해주세요")
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
