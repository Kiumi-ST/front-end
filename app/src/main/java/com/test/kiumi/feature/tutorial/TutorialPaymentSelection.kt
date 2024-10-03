package com.test.kiumi.feature.tutorial

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.test.kiumi.R
import java.util.*

class TutorialPaymentSelection : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_payment_selection)

        // 이전 액티비티에서 전달된 isTTSActive 값 받아오기 (기본값 true)
        isTTSActive = intent.getBooleanExtra("isTTSActive", false)

        // 현재 isTTSActive 값을 로그로 출력
        Log.d("TutorialPaymentSelection", "onCreate: isTTSActive = $isTTSActive")

        // TTS 초기화
        tts = TextToSpeech(this, this)

        // 카드 결제에 클릭 리스너 추가
        val cardPaymentLayout: LinearLayout = findViewById(R.id.linearLayoutCardPayment)
        cardPaymentLayout.setOnClickListener {
            // ActualPracticePayment 액티비티로 이동
            val intent = Intent(this, TutorialPaymentCard::class.java)
            intent.putExtra("isTTSActive", isTTSActive) // 다음 액티비티로 isTTSActive 값 전달
            startActivity(intent)
        }

        // 모바일 상품권에 클릭 리스너 추가
        val giftCardLayout: LinearLayout = findViewById(R.id.linearLayoutGiftCard)
        giftCardLayout.setOnClickListener {
            // 모바일 상품권 기능 추가 (현재는 빈 동작)
        }

        // 이전 단계 버튼에 클릭 리스너 추가
        val buttonPreviousStep: Button = findViewById(R.id.buttonPreviousStep)
        buttonPreviousStep.setOnClickListener {
            val intent = Intent(this, TutorialMainActivity::class.java)
            intent.putExtra("isTTSActive", isTTSActive) // 이전 액티비티로 돌아갈 때도 isTTSActive 값 전달
            startActivity(intent)
        }

        // 처음으로 버튼에 클릭 리스너 추가
        val buttonHome: Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            // 홈 화면으로 이동하는 기능 추가 (현재는 빈 동작)
        }

        // 도움 기능 버튼에 클릭 리스너 추가
        val buttonHelp: LinearLayout = findViewById(R.id.linearLayoutHelp)
        buttonHelp.setOnClickListener {
            // 도움 기능 추가 (현재는 빈 동작)
        }

        // LinearLayout에 애니메이션 설정
        val takeOutLayout: LinearLayout = findViewById(R.id.linearLayoutCardPayment)
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
                speakText("카드 결제를 클릭해주세요")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // 현재 isTTSActive 값을 로그로 출력
        Log.d("TutorialPaymentSelection", "onResume: isTTSActive = $isTTSActive")

        // 액티비티가 다시 포커스를 얻을 때 TTS 활성화 상태에 따라 음성 출력
        if (isTTSActive && ::tts.isInitialized) {
            speakText("카드 결제를 클릭해주세요")
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
