package com.example.kiumi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ProposalSideMenuSelectionActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    private lateinit var menuItem: ProposalMenuItem
    private var isLargeSet: Boolean = false
    private lateinit var selectedSide: ProposalMenuItem

    private val sideMenuItems = listOf(
        ProposalMenuItem("후렌치 후라이 - 미디엄", "₩0", R.drawable.french_fries_medium, false),
        ProposalMenuItem("코울슬로", "₩200", R.drawable.coleslaw, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal_side_menu_selection)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        menuItem = intent.getParcelableExtra("menuItem") ?: return
        isLargeSet = intent.getBooleanExtra("isLargeSet", false)

        val baseSetPrice = menuItem.price.replace("₩", "").replace(",", "").toInt()
        val setPrice = if (isLargeSet) baseSetPrice + 2700 else baseSetPrice + 2000

        findViewById<TextView>(R.id.title).text = "${menuItem.name} - ${if (isLargeSet) "라지세트" else "세트"}"

        findViewById<LinearLayout>(R.id.button_side1).setOnClickListener {
            selectedSide = sideMenuItems[0]
            goToDrinkSelection()
        }

        findViewById<LinearLayout>(R.id.button_side2).setOnClickListener {
            selectedSide = sideMenuItems[1]
            goToDrinkSelection()
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, ProposalMainActivity::class.java)
                .apply { putExtra("previous_activity", "개선안_버거 선택-세트 사이드") }
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
                putString("screen_name", "개선안_버거 선택-세트 사이드") // 현재 화면 이름
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
            putString("screen_name", "개선안_버거 선택-세트 사이드")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

    private fun goToDrinkSelection() {
        val intent = Intent(this, ProposalDrinkSelectionActivity::class.java).apply {
            putExtra("menuItem", menuItem)
            putExtra("isLargeSet", isLargeSet)
            putExtra("selectedSide", selectedSide)
            putExtra("previous_activity", "개선안_버거 선택-세트 사이드")
        }
        startActivity(intent)
    }
}