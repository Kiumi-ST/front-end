package com.example.kiumi

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class TutorialBurgerSetOrderActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var preferences: SharedPreferences
    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false // 기본값을 false로 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial_burger_set_order)

        // 이전 액티비티에서 전달된 isTTSActive 값 받아오기 (기본값 false)
        isTTSActive = intent.getBooleanExtra("isTTSActive", false)

        // TTS 초기화
        tts = TextToSpeech(this, this)

        preferences = getSharedPreferences("com.example.kiumi.PREFERENCES", MODE_PRIVATE)

        val quantityText: TextView = findViewById(R.id.quantity)

        findViewById<Button>(R.id.button_add_to_cart).setOnClickListener {
            // burger_set_clicked 상태를 true로 저장
            preferences.edit().putBoolean("burger_set_clicked", true).apply()

            val intent = Intent(this, TutorialCartAddedActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive) // 메인으로 돌아갈 때도 isTTSActive 값 전달
            intent.putExtra("isTTSActiveForMainActivity", false) // MainActivity에서 TTS를 비활성화
            startActivity(intent)
        }


        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, TutorialMainActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive) // 메인으로 돌아갈 때도 isTTSActive 값 전달
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_decrease_quantity).setOnClickListener {
            // 수량 감소 로직 추가
        }

        findViewById<Button>(R.id.button_increase_quantity).setOnClickListener {
            // 수량 증가 로직 추가
        }

        // Button에 애니메이션 설정
        val addToCartButton: Button = findViewById(R.id.button_add_to_cart)
        addToCartButton.setBackgroundResource(R.drawable.blinking_border_animation_yellow)
        val animationDrawable = addToCartButton.background as AnimationDrawable
        animationDrawable.start()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 언어가 지원되지 않음
            } else if (isTTSActive) {
                // TTS가 활성화된 경우 음성 출력
                speakText("장바구니 추가를 클릭해주세요")
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
            speakText("장바구니 추가를 클릭해주세요")
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
