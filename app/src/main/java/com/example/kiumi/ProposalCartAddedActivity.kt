package com.example.kiumi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ProposalCartAddedActivity : PopupActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    // Handler와 Runnable을 클래스 변수로 정의
    private val handler = Handler(Looper.getMainLooper())
    private val navigateRunnable = Runnable {
        val intent = Intent(
            this@ProposalCartAddedActivity,
            ProposalMainActivity::class.java
        ).apply {
            putExtra("previous_activity", "개선안_장바구니 추가 완료")
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal_cart_added)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        val price: TextView = findViewById(R.id.price)

        // 인텐트에서 데이터를 받아옴
        val itemPrice = intent.getStringExtra("ITEM_PRICE")
        val itemQuantity = intent.getIntExtra("ITEM_QUANTITY", 1)

        // 데이터 설정
        if (itemPrice != null && itemQuantity != null) {
            val totalPrice = itemPrice.replace("₩", "").replace(",", "").toInt() * itemQuantity
            price.text = "₩${totalPrice}"
        } else {
            price.text = ""
        }

        // 1.5초 후에 이동
        //Toast.makeText(this, "1.5초 동안 화면이 유지됩니다", Toast.LENGTH_LONG).show()
        handler.postDelayed(navigateRunnable, 1500)

        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    // 뒤로 가기 버튼을 눌렀을 때 실행되는 콜백 메소드
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            ProposalCartManager.removeLastItem() // 장바구니에 마지막으로 추가한 메뉴 없앰

            val params = Bundle().apply {
                putString("previous_screen_name", previousActivity) // 잘못 클릭했던 화면 이름
                putString("screen_name", "개선안_장바구니 추가 완료") // 현재 화면 이름
            }
            firebaseAnalytics.logEvent("go_back", params)

            // 예약된 3초 후 이동 작업을 취소
            handler.removeCallbacks(navigateRunnable)

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
            putString("screen_name", "개선안_장바구니 추가 완료")
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