package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

class ProposalPlaceSelection : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_proposal_place_selection)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics
        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            val intent = Intent(
                this@ProposalPlaceSelection,
                ProposalQRSuccess::class.java
            ).apply { putExtra("previous_activity", "개선안_장소") }
            startActivity(intent)
        }
        
        // 매장 식사 버튼 클릭 시
        findViewById<LinearLayout>(R.id.button_dine_in).setOnClickListener {
            firebaseAnalytics.logEvent("select_dining_option"){
                param(FirebaseAnalytics.Param.CONTENT, "dine_in")
            }
            startActivity(Intent(this, ProposalMainActivity::class.java))
        }

        // 포장 버튼 클릭 시
        findViewById<LinearLayout>(R.id.button_take_out).setOnClickListener {
            firebaseAnalytics.logEvent("select_dining_option"){
                param(FirebaseAnalytics.Param.CONTENT, "take_out")
            }
        }

        // 처음으로 버튼 클릭 시
        findViewById<Button>(R.id.buttonHome).setOnClickListener {
        }

        // 도움 기능 버튼 클릭 시
        findViewById<LinearLayout>(R.id.linearLayoutHelp).setOnClickListener {
        }

        // 영어 버튼 클릭 시
        findViewById<Button>(R.id.button_english).setOnClickListener {
        }

        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    // 뒤로 가기 버튼을 눌렀을 때 실행되는 콜백 메소드
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            val params = Bundle().apply {
                putString("previous_screen_name", previousActivity) // 잘못 클릭했던 화면 이름
                putString("screen_name", "개선안_장소") // 현재 화면 이름
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
            putString("screen_name", "개선안_장소")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }
}