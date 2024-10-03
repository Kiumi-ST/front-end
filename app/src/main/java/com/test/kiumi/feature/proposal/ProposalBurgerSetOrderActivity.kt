package com.test.kiumi.feature.proposal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.test.kiumi.feature.common.PhotoCaptureService
import com.test.kiumi.feature.common.PopupActivity
import com.test.kiumi.data.ProposalCartManager
import com.test.kiumi.data.ProposalMenuItem
import com.test.kiumi.data.ProposalOrderItem
import com.test.kiumi.R
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ProposalBurgerSetOrderActivity : PopupActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var menuItem: ProposalMenuItem
    private var isLargeSet: Boolean = false
    private lateinit var selectedSide: ProposalMenuItem
    private lateinit var selectedDrink: ProposalMenuItem
    private var quantity = 1
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal_burger_set_order)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        menuItem = intent.getParcelableExtra("menuItem") ?: return
        isLargeSet = intent.getBooleanExtra("isLargeSet", false)
        selectedSide = intent.getParcelableExtra("selectedSide") ?: return
        selectedDrink = intent.getParcelableExtra("selectedDrink") ?: return

        val quantityText: TextView = findViewById(R.id.quantity)

        val baseSetPrice = menuItem.price.replace("₩", "").replace(",", "").toInt()
        val setPrice = if (isLargeSet) baseSetPrice + 2700 else baseSetPrice + 2000
        val totalPrice = setPrice + selectedSide.price.replace("₩", "").replace(",", "").toInt()

        findViewById<TextView>(R.id.title).text = "${menuItem.name} - ${if (isLargeSet) "라지세트" else "세트"}"
        findViewById<TextView>(R.id.title2).text = "₩$totalPrice"
        findViewById<TextView>(R.id.burger_info).text = "${menuItem.name}"
        findViewById<TextView>(R.id.side_info).text = "${selectedSide.name}"
        findViewById<TextView>(R.id.drink_info).text = "${selectedDrink.name}"
        findViewById<ImageView>(R.id.burger_image).setImageResource(menuItem.imageResourceId)
        findViewById<ImageView>(R.id.side_image).setImageResource(selectedSide.imageResourceId)
        findViewById<ImageView>(R.id.drink_image).setImageResource(selectedDrink.imageResourceId)


        findViewById<Button>(R.id.button_modify_burger).setOnClickListener {
            val intent = Intent(this, ProposalBurgerCustomizationActivity::class.java)
                .apply {
                    putExtra("menuItem", menuItem)
                    putExtra("previous_activity", "개선안_버거 선택-세트 확인")
                }
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_modify_side).setOnClickListener {
            val intent = Intent(this, ProposalSideMenuSelectionActivity::class.java)
                .apply {
                    putExtra("menuItem", menuItem)
                    putExtra("isLargeSet", isLargeSet)
                    putExtra("selectedDrink", selectedDrink)
                    putExtra("previous_activity", "개선안_버거 선택-세트 확인")
                }
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_modify_drink).setOnClickListener {
            val intent = Intent(this, ProposalDrinkSelectionActivity::class.java)
                .apply {
                    putExtra("menuItem", menuItem)
                    putExtra("isLargeSet", isLargeSet)
                    putExtra("selectedSide", selectedSide)
                    putExtra("previous_activity", "개선안_버거 선택-세트 확인")
                }
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.button_add_to_cart).setOnClickListener {
            val orderItem = ProposalOrderItem(menuItem, quantity, true, isLargeSet, selectedSide.name, selectedDrink.name, totalPrice.toString())
            ProposalCartManager.addItem(orderItem)
            val intent = Intent(this, ProposalCartAddedActivity::class.java)
                .apply {
                    putExtra("ITEM_PRICE", totalPrice.toString())
                    putExtra("ITEM_QUANTITY", quantity)
                    putExtra("previous_activity", "개선안_버거 선택-세트 확인")
                }
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_nutrition_info).setOnClickListener {
            val intent = Intent(
                this,
                ProposalNutritionInfoActivity::class.java
            ).apply {
                putExtra("ITEM_NAME", menuItem.name)
                putExtra("ITEM_IMAGERESID", menuItem.imageResourceId)
                putExtra("previous_activity", "개선안_버거 선택-세트 확인")
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, ProposalMainActivity::class.java)
                .apply { putExtra("previous_activity", "개선안_버거 선택-세트 확인") }
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_decrease_quantity).setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityText.text = quantity.toString()
            }
        }
        findViewById<Button>(R.id.button_increase_quantity).setOnClickListener {
            quantity++
            quantityText.text = quantity.toString()
        }

        // 처음으로 버튼 클릭 시
        findViewById<TextView>(R.id.gotohome).setOnClickListener {
            val intent = Intent(this, ProposalOrderCancelActivity::class.java)
                .apply { putExtra("previous_activity", "개선안_버거 선택-세트 확인 ") }
            startActivity(intent)
        }

        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            val params = Bundle().apply {
                putString("previous_screen_name", previousActivity) // 잘못 클릭했던 화면 이름
                putString("screen_name", "개선안_버거 선택-세트 확인") // 현재 화면 이름
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
            putString("screen_name", "개선안_버거 선택-세트 확인")
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