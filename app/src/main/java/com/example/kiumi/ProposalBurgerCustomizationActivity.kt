package com.example.kiumi

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ProposalBurgerCustomizationActivity : PopupActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    private lateinit var menuItem: ProposalMenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal_burger_customization)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        menuItem = intent.getParcelableExtra("menuItem") ?: return

        findViewById<TextView>(R.id.menu_text).text = menuItem.name
        findViewById<TextView>(R.id.menu_kcal_text).text = "${menuItem.price}"
        findViewById<ImageView>(R.id.menu_image).setImageResource(menuItem.imageResourceId)

        Toast.makeText(this, "해당 정보는 임의의 정보로, 정확하지 않을 수 있습니다.", Toast.LENGTH_LONG).show()

        // 초기 값 설정
        var quantity1 = 1
        var quantity2 = 1
        var quantity3 = 1
        var quantity4 = 1

        // 양상추
        val buttonDecreaseQuantity1: Button = findViewById(R.id.button_decrease_quantity_1)
        val buttonIncreaseQuantity1: Button = findViewById(R.id.button_increase_quantity_1)
        val quantityText1: TextView = findViewById(R.id.quantity_1)
        buttonDecreaseQuantity1.setOnClickListener {
            if (quantity1 > 0) {
                quantity1 = 0
                updateButtonsAndText(quantity1, buttonDecreaseQuantity1, buttonIncreaseQuantity1, quantityText1)
            }
        }
        buttonIncreaseQuantity1.setOnClickListener {
            if (quantity1 < 1) {
                quantity1 = 1
                updateButtonsAndText(quantity1, buttonDecreaseQuantity1, buttonIncreaseQuantity1, quantityText1)
            }
        }

        // 함박패티
        val buttonDecreaseQuantity2: Button = findViewById(R.id.button_decrease_quantity_2)
        val buttonIncreaseQuantity2: Button = findViewById(R.id.button_increase_quantity_2)
        val quantityText2: TextView = findViewById(R.id.quantity_2)
        buttonDecreaseQuantity2.setOnClickListener {
            if (quantity2 > 0) {
                quantity2 = 0
                updateButtonsAndText(quantity2, buttonDecreaseQuantity2, buttonIncreaseQuantity2, quantityText2)
            }
        }
        buttonIncreaseQuantity2.setOnClickListener {
            if (quantity2 < 1) {
                quantity2 = 1
                updateButtonsAndText(quantity2, buttonDecreaseQuantity2, buttonIncreaseQuantity2, quantityText2)
            }
        }

        // 마요 소스
        val buttonDecreaseQuantity3: Button = findViewById(R.id.button_decrease_quantity_3)
        val buttonIncreaseQuantity3: Button = findViewById(R.id.button_increase_quantity_3)
        val quantityText3: TextView = findViewById(R.id.quantity_3)
        buttonDecreaseQuantity3.setOnClickListener {
            if (quantity3 > 0) {
                quantity3 = 0
                updateButtonsAndText(quantity3, buttonDecreaseQuantity3, buttonIncreaseQuantity3, quantityText3)
            }
        }
        buttonIncreaseQuantity3.setOnClickListener {
            if (quantity3 < 1) {
                quantity3 = 1
                updateButtonsAndText(quantity3, buttonDecreaseQuantity3, buttonIncreaseQuantity3, quantityText3)
            }
        }

        // 불고기 소스
        val buttonDecreaseQuantity4: Button = findViewById(R.id.button_decrease_quantity_4)
        val buttonIncreaseQuantity4: Button = findViewById(R.id.button_increase_quantity_4)
        val quantityText4: TextView = findViewById(R.id.quantity_4)
        buttonDecreaseQuantity4.setOnClickListener {
            if (quantity4 > 0) {
                quantity4 = 0
                updateButtonsAndText(quantity4, buttonDecreaseQuantity4, buttonIncreaseQuantity4, quantityText4)
            }
        }
        buttonIncreaseQuantity4.setOnClickListener {
            if (quantity4 < 1) {
                quantity4 = 1
                updateButtonsAndText(quantity4, buttonDecreaseQuantity4, buttonIncreaseQuantity4, quantityText4)
            }
        }

        updateButtonsAndText(quantity1, buttonDecreaseQuantity1, buttonIncreaseQuantity1, quantityText1)
        updateButtonsAndText(quantity2, buttonDecreaseQuantity2, buttonIncreaseQuantity2, quantityText2)
        updateButtonsAndText(quantity3, buttonDecreaseQuantity3, buttonIncreaseQuantity3, quantityText3)
        updateButtonsAndText(quantity4, buttonDecreaseQuantity4, buttonIncreaseQuantity4, quantityText4)

        findViewById<Button>(R.id.button_back).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.button_save).setOnClickListener {
            finish()
        }

        // 처음으로 버튼 클릭 시
        findViewById<TextView>(R.id.gotohome).setOnClickListener {
            val intent = Intent(this, ProposalOrderCancelActivity::class.java).apply { putExtra("previous_activity", "개선안_영양정보") }
            startActivity(intent)
        }

        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun updateButtonsAndText(quantity: Int, decreaseButton: Button, increaseButton: Button, quantityText: TextView) {
        quantityText.text = quantity.toString()

        if (quantity == 0) {
            decreaseButton.setBackgroundColor(Color.GRAY)
            increaseButton.setBackgroundResource(R.drawable.button_background)
        } else if (quantity == 1) {
            increaseButton.setBackgroundColor(Color.GRAY)
            decreaseButton.setBackgroundResource(R.drawable.button_background)
        }
    }

    // 뒤로 가기 버튼을 눌렀을 때 실행되는 콜백 메소드
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            val params = Bundle().apply {
                putString("previous_screen_name", previousActivity)
                putString("screen_name", "개선안_주문 취소")
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
            putString("screen_name", "개선안_영양정보")
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