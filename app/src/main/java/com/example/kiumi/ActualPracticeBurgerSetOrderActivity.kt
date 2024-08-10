package com.example.kiumi;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ActualPracticeBurgerSetOrderActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var menuItem: MenuItem
    private var isLargeSet: Boolean = false
    private lateinit var selectedSide: MenuItem
    private lateinit var selectedDrink: MenuItem
    private var quantity = 1
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_burger_set_order)

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
        val totalCalories = menuItem.calories.replace(" Kcal", "").toInt() + selectedSide.calories.replace(" Kcal", "").toInt() + selectedDrink.calories.replace(" Kcal", "").toInt()

        findViewById<TextView>(R.id.title).text = "${menuItem.name} - ${if (isLargeSet) "라지세트" else "세트"}"
        findViewById<TextView>(R.id.title2).text = "₩$totalPrice $totalCalories Kcal"
        findViewById<TextView>(R.id.burger_info).text = "${menuItem.name} \n ${menuItem.calories}"
        findViewById<TextView>(R.id.side_info).text = "${selectedSide.name} \n ${selectedSide.calories}"
        findViewById<TextView>(R.id.drink_info).text = "${selectedDrink.name} \n ${selectedDrink.calories}"
        findViewById<ImageView>(R.id.burger_image).setImageResource(menuItem.imageResourceId)
        findViewById<ImageView>(R.id.side_image).setImageResource(selectedSide.imageResourceId)
        findViewById<ImageView>(R.id.drink_image).setImageResource(selectedDrink.imageResourceId)

        findViewById<Button>(R.id.button_add_to_cart).setOnClickListener {
            val orderItem = OrderItem(menuItem, quantity, true, isLargeSet, selectedSide.name, selectedDrink.name, totalPrice.toString(), totalCalories.toString())
            CartManager.addItem(orderItem)
            val intent = Intent(this, ActualPracticeCartAddedActivity::class.java)
                .apply {
                    putExtra("ITEM_PRICE", totalPrice.toString())
                    putExtra("ITEM_QUANTITY", quantity)
                    putExtra("previous_activity", "실전 연습_버거 선택-세트 확인")
                }
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_nutrition_info).setOnClickListener {
            val intent = Intent(
                this,
                ActualPracticeNutritionInfoActivity::class.java
            ).apply {
                putExtra("ITEM_NAME", menuItem.name)
                putExtra("ITEM_IMAGERESID", menuItem.imageResourceId)
                putExtra("previous_activity", "실전 연습_버거 선택-세트 확인")
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, ActualPracticeMainActivity::class.java)
                .apply { putExtra("previous_activity", "실전 연습_버거 선택-세트 확인") }
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
            val intent = Intent(this, ActualPracticeOrderCancelActivity::class.java)
                .apply { putExtra("previous_activity", "실전 연습_버거 선택-세트 확인 ") }
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
                putString("screen_name", "실전 연습_버거 선택-세트 확인") // 현재 화면 이름
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
            putString("screen_name", "실전 연습_버거 선택-세트 확인")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }
}
