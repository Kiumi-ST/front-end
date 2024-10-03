package com.test.kiumi.feature.proposal

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.kiumi.feature.common.PhotoCaptureService
import com.test.kiumi.data.PointManager
import com.test.kiumi.feature.common.PopupActivity
import com.test.kiumi.data.ProposalCartManager
import com.test.kiumi.adapter.ProposalOrderAdapter
import com.test.kiumi.data.ProposalOrderItem
import com.test.kiumi.R
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ProposalOrderActivity : PopupActivity() {
    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderAdapter: ProposalOrderAdapter
    private lateinit var orderItems: MutableList<ProposalOrderItem>
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal_order)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics
        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        orderRecyclerView = findViewById(R.id.order_recycler_view)
        orderRecyclerView.layoutManager = LinearLayoutManager(this)

        // 샘플 데이터 생성
//        val menuItem = MenuItem("맥스파이시® 상하이 버거 - 세트", "₩5,900", "826 Kcal",R.drawable.ic_burger_set, true)
//        val menuItem2 = MenuItem("불고기 버거 - 세트", "₩4,500", "700 Kcal", R.drawable.ic_burger_set, false)

//        orderItems = mutableListOf(
//            OrderItem(menuItem) ,
//            OrderItem(menuItem2)
//            // 더 많은 아이템 추가 가능
//        )
        orderItems = ProposalCartManager.getItems().toMutableList()

        orderAdapter = ProposalOrderAdapter(orderItems) { itemToRemove ->
            ProposalCartManager.removeItem(itemToRemove)
            orderItems.remove(itemToRemove)
            orderAdapter.notifyDataSetChanged()
            updateTotalPrice()
        }
        orderRecyclerView.adapter = orderAdapter

        ProposalCartManager.addListener { updateTotalPrice() }
        updateTotalPrice()

        findViewById<TextView>(R.id.order_more).setOnClickListener {
            val intent =
                Intent(this@ProposalOrderActivity, ProposalMainActivity::class.java).apply { putExtra("previous_activity", "개선안_주문 내역") }
            startActivity(intent)
        }

        findViewById<TextView>(R.id.order_finish).setOnClickListener {
            val intent = Intent(this, ProposalPaymentSelection::class.java).apply { putExtra("previous_activity", "개선안_주문 내역") }
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            PointManager.setPointEarned(true)
            val intent =
                Intent(this@ProposalOrderActivity, ProposalQRSuccess::class.java).apply { putExtra("previous_activity", "개선안_주문 내역") }
            startActivity(intent)
        }

        // 처음으로 버튼 클릭 시
        findViewById<TextView>(R.id.gotohome).setOnClickListener {
            val intent = Intent(this, ProposalOrderCancelActivity::class.java).apply { putExtra("previous_activity", "개선안_주문 내역") }
            startActivity(intent)
        }

        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        ProposalCartManager.removeListener { updateTotalPrice() }
    }

    private fun updateTotalPrice() {
        val sumPriceTextView: TextView = findViewById(R.id.sumif)
        val totalPriceTextView: TextView = findViewById(R.id.sumall)
        sumPriceTextView.text = ProposalCartManager.getTotalPrice()
        totalPriceTextView.text = ProposalCartManager.getTotalPrice()
    }

    // 뒤로 가기 버튼을 눌렀을 때 실행되는 콜백 메소드
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            val params = Bundle().apply {
                putString("previous_screen_name", previousActivity)
                putString("screen_name", "개선안_주문 내역")
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
            putString("screen_name", "개선안_주문 내역")
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