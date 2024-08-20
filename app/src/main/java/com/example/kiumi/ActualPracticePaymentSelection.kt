package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.kiumi.R
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ActualPracticePaymentSelection : PopupActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_payment_selection)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

      
        if (PointManager.isPointEarned()){
            findViewById<TextView>(R.id.textViewSubtitle).text = ""
        }
        
        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")
        

        // 카드 결제에 클릭 리스너 추가
        val cardPaymentLayout: LinearLayout = findViewById(R.id.linearLayoutCardPayment)
        cardPaymentLayout.setOnClickListener {
            // ActualPracticePayment 액티비티로 이동
            val intent = Intent(this, ActualPracticePaymentCard::class.java).apply { putExtra("previous_activity", "실전 연습_결제 방법") }
            startActivity(intent)
        }

        // 모바일 상품권에 클릭 리스너 추가
        val giftCardLayout: LinearLayout = findViewById(R.id.linearLayoutGiftCard)
        giftCardLayout.setOnClickListener {
            // ActualPracticePaymentMobileGiftScan 액티비티로 이동
            val intent = Intent(this, ActualPracticePaymentMobileGiftScan::class.java).apply { putExtra("previous_activity", "실전 연습_결제 방법") }
            startActivity(intent)
        }

        // 이전 단계 버튼에 클릭 리스너 추가
        val buttonPreviousStep: Button = findViewById(R.id.buttonPreviousStep)
        buttonPreviousStep.setOnClickListener {
            finish()
        }

        // 처음으로 버튼에 클릭 리스너 추가
        val buttonHome: Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this, ActualPracticeOrderCancelActivity::class.java)
                .apply { putExtra("previous_activity", "실전 연습_결제 방법") }
            startActivity(intent)
        }

        // 도움 기능 버튼에 클릭 리스너 추가
        val buttonHelp: LinearLayout = findViewById(R.id.linearLayoutHelp)
        buttonHelp.setOnClickListener {
            // 도움 기능 액티비티로 이동 (변경 필요)
            // val intent = Intent(this, HelpActivity::class.java)
            // startActivity(intent).apply { putExtra("previous_activity", "실전 연습_결제 방법") }
            // 현재는 단순히 토스트 메시지로 대체
            Toast.makeText(this, "도움 기능으로 이동", Toast.LENGTH_SHORT).show()
        }

        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }

    // 뒤로 가기 버튼을 눌렀을 때 실행되는 콜백 메소드
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            val params = Bundle().apply {
                putString("previous_screen_name", previousActivity)
                putString("screen_name", "실전 연습_결제 방법")
            }
            firebaseAnalytics.logEvent("go_back", params)

            // 실제로 뒤로 가기 동작을 수행하도록 추가
            isEnabled = false // 콜백을 비활성화하여 기본 뒤로 가기 동작을 수행
            onBackPressedDispatcher.onBackPressed() // 기본 뒤로 가기 동작 수행
        }
    }

    private fun notifyServiceOfCurrentActivity() {
        val serviceIntent = Intent(this, PhotoCaptureService::class.java)
        serviceIntent.putExtra("ACTIVITY_NAME", this::class.java.simpleName)
        // 이미 실행 중이면 onStartCommand만 호출됨
        startService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()
        notifyServiceOfCurrentActivity()
    }

    override fun onStart() {
        super.onStart()
        startTime = System.currentTimeMillis()
    }

    override fun onStop() {
        super.onStop()
        endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        val params = Bundle().apply {
            putLong("screen_duration", duration)
            putString("screen_name", "실전 연습_결제 방법")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

}
