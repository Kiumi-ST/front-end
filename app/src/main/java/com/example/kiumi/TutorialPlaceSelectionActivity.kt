package com.example.kiumi

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale;


class TutorialPlaceSelectionActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var preferences: SharedPreferences
    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial_place_selection)

        // SharedPreferences 초기화 및 burger_set_clicked 값 초기화
        preferences = getSharedPreferences("com.example.kiumi.PREFERENCES", MODE_PRIVATE)
        preferences.edit().putBoolean("burger_set_clicked", false).apply()

        // Intent로 전달된 isTTSActive 값 받기
        isTTSActive = intent.getBooleanExtra("isTTSActive", false)  // 기본값 false

        // TTS 초기화
        tts = TextToSpeech(this, this)

        Log.d("TTS", "isTTSActive: $isTTSActive")  // TTS 활성화 여부 로그

        // LinearLayout에 애니메이션 설정
        val takeOutLayout: LinearLayout = findViewById(R.id.button_take_out)
        takeOutLayout.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = takeOutLayout.background as AnimationDrawable
        animationDrawable.start()

        // 포장 버튼 클릭 시
        takeOutLayout.setOnClickListener {
            // isTTSActive 값을 다음 액티비티에 전달
            val intent = Intent(this, TutorialMainActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive)
            startActivity(intent)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA) {
                Log.e("TTS", "언어 데이터가 없습니다.")
            } else if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "이 언어는 지원되지 않습니다.")
            } else {
                if (isTTSActive) {
                    speakText("테이크 아웃을 클릭해주세요")
                }
            }
        } else {
            Log.e("TTS", "TTS 초기화 실패")
        }
    }

    override fun onResume() {
        super.onResume()
        // 액티비티가 다시 포커스를 얻을 때 TTS 활성화 상태에 따라 음성 출력
        if (isTTSActive && ::tts.isInitialized) {
            speakText("테이크 아웃을 클릭해주세요")
        }
    }

    private fun speakText(text: String) {
        if (isTTSActive) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            Log.d("TTS", "음성 출력: $text")
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
