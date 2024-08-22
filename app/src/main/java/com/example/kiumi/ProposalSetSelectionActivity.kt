package com.example.kiumi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ProposalSetSelectionActivity : PopupActivity() {

    private lateinit var menuItem: ProposalMenuItem
    private var isLargeSet: Boolean = false

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal_set_selection)

        menuItem = intent.getParcelableExtra("menuItem") ?: return

        val titleTextView: TextView = findViewById(R.id.title)
        val setTextView: TextView = findViewById(R.id.button_set_text_title)
        val largeSetTextView: TextView = findViewById(R.id.button_large_set_text_title)
        val setPriceTextView: TextView = findViewById(R.id.button_set_text_price)
        val largerSetPriceTextView: TextView = findViewById(R.id.button_large_set_text_price)

        val baseSetPrice = menuItem.price.replace("₩", "").replace(",", "").toInt()

        titleTextView.text = "${menuItem.name}"
        setTextView.text = "세트"
        largeSetTextView.text = "라지세트"
        setPriceTextView.text = "₩${baseSetPrice + 2000}"
        largerSetPriceTextView.text = "₩${baseSetPrice + 2700}"

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
            val intent = Intent(this, ProposalMainActivity::class.java)
                .apply { putExtra("previous_activity", "개선안_버거 선택-세트 사이즈") }
            startActivity(intent)
        }

        // 처음으로 버튼 클릭 시
        findViewById<TextView>(R.id.gotohome).setOnClickListener {
            val intent = Intent(this, ProposalOrderCancelActivity::class.java)
                .apply { putExtra("previous_activity", "개선안_버거 선택-세트 사이즈") }
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
                putString("screen_name", "개선안_버거 선택-세트 사이즈") // 현재 화면 이름
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
            putString("screen_name", "개선안_버거 선택-세트 사이즈")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

    private fun goToSideMenuSelection() {
        val intent = Intent(this, ProposalSideMenuSelectionActivity::class.java).apply {
            putExtra("menuItem", menuItem)
            putExtra("isLargeSet", isLargeSet)
            putExtra("previous_activity", "개선안_버거 선택-세트 사이즈")
        }
        startActivity(intent)
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