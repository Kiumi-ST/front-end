package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ActualPracticeNutritionInfoActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_nutrition_info)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        val itemName = intent.getStringExtra("ITEM_NAME")
        val itemImageResourceId = intent.getIntExtra("ITEM_IMAGERESID", 0)

        findViewById<TextView>(R.id.menu_text).text = itemName
        findViewById<ImageView>(R.id.menu_image).setImageResource(itemImageResourceId)

        Toast.makeText(this, "해당 정보는 임의의 정보로, 정확하지 않을 수 있습니다.", Toast.LENGTH_LONG).show()

        // 상세 정보 토글
        findViewById<ImageView>(R.id.toggleButton).setOnClickListener {
            if (findViewById<LinearLayout>(R.id.detailInfoLayout).visibility == View.GONE) {
                findViewById<LinearLayout>(R.id.detailInfoLayout).visibility = View.VISIBLE
                findViewById<ImageView>(R.id.toggleButton).setImageResource(R.drawable.ic_uparrow)
            } else {
                findViewById<LinearLayout>(R.id.detailInfoLayout).visibility = View.GONE
                findViewById<ImageView>(R.id.toggleButton).setImageResource(R.drawable.ic_downarrow)
            }
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            finish()
        }

        // 처음으로 버튼 클릭 시
        findViewById<TextView>(R.id.gotohome).setOnClickListener {
            val intent = Intent(this, ActualPracticeOrderCancelActivity::class.java).apply { putExtra("previous_activity", "실전 연습_영양정보") }
            startActivity(intent)
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
                putString("screen_name", "실전 연습_영양정보")
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
            putString("screen_name", "실전 연습_영양정보")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }
}