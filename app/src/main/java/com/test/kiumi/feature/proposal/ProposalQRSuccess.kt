package com.test.kiumi.feature.proposal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import com.test.kiumi.feature.common.PhotoCaptureService
import com.test.kiumi.feature.common.PopupActivity
import com.test.kiumi.R
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ProposalQRSuccess : PopupActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_proposal_qrsuccess)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        // 확인 버튼을 ID로 찾기
        val buttonConfirm: Button = findViewById(R.id.buttonConfirm)

        // 버튼에 클릭 리스너 설정
        buttonConfirm.setOnClickListener {
            finish()
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
                putString("screen_name", "개선안_QR 코드 스캔 시")
            }
            firebaseAnalytics.logEvent("go_back", params)

            // 실제로 뒤로 가기 동작을 수행하도록 추가
            isEnabled = false // 콜백을 비활성화하여 기본 뒤로 가기 동작을 수행
            onBackPressedDispatcher.onBackPressed() // 기본 뒤로 가기 동작 수행
        }
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
            putString("screen_name", "개선안_QR 코드 스캔 시")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
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
}