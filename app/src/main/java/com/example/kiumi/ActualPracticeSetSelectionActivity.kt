package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ActualPracticeSetSelectionActivity : AppCompatActivity() {
  
    private lateinit var menuItem: MenuItem
    private var isLargeSet: Boolean = false
  
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_set_selection)

        menuItem = intent.getParcelableExtra("menuItem") ?: return

        val titleTextView: TextView = findViewById(R.id.title)
        val setTextView: TextView = findViewById(R.id.button_set_text_title)
        val largeSetTextView: TextView = findViewById(R.id.button_large_set_text_title)
        val setPriceTextView: TextView = findViewById(R.id.button_set_text_price_calories)
        val largerSetPriceTextView: TextView = findViewById(R.id.button_large_set_text_price_calories)

        val baseSetPrice = menuItem.price.replace("₩", "").replace(",", "").toInt()

        titleTextView.text = "${menuItem.name}"
        setTextView.text = "${menuItem.name} - 세트"
        largeSetTextView.text = "${menuItem.name} - 라지세트"
        setPriceTextView.text = "₩${baseSetPrice + 2000} ${menuItem.calories}"
        largerSetPriceTextView.text = "₩${baseSetPrice + 2700} ${menuItem.calories}"

        findViewById<LinearLayout>(R.id.button_set).setOnClickListener {
            isLargeSet = false
            goToSideMenuSelection()
        }

        findViewById<LinearLayout>(R.id.button_large_set).setOnClickListener {
            isLargeSet = true
            goToSideMenuSelection()
        }
        
        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, ActualPracticeMainActivity::class.java)
                .apply { putExtra("previous_activity", "실전 연습_버거 선택-세트") }
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
                putString("previous_screen_name", previousActivity) // 잘못 클릭했던 화면 이름
                putString("screen_name", "실전 연습_버거 선택-세트") // 현재 화면 이름
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
            putString("screen_name", "실전 연습_버거 선택-세트")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

    private fun goToSideMenuSelection() {
        val intent = Intent(this, ActualPracticeSideMenuSelectionActivity::class.java).apply {
            putExtra("menuItem", menuItem)
            putExtra("isLargeSet", isLargeSet)
            putExtra("previous_activity", "실전 연습_버거 선택-세트")
        }
        startActivity(intent)
    }
}