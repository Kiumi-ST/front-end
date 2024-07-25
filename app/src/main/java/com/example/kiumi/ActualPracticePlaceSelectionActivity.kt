package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

class ActualPracticePlaceSelectionActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var quantity = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_place_selection)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics
        val popupBurgerSelectionContainer: RelativeLayout = findViewById(R.id.popup_burger_selection)
        val popupSingBurgerContainer: RelativeLayout = findViewById(R.id.popup_single_burger)
        val quantityText: TextView = findViewById(R.id.quantity)

        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            val intent = Intent(
                this@ActualPracticePlaceSelectionActivity,
                ActualPracticeQRSuccess::class.java
            )
            startActivity(intent)
        }

        // 매장 식사 버튼 클릭 시
        findViewById<LinearLayout>(R.id.button_dine_in).setOnClickListener {
            firebaseAnalytics.logEvent("select_dining_option"){
                param(FirebaseAnalytics.Param.CONTENT, "dine_in")
            }
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

        // 영어 버튼 클릭 시 (임시로 세트/단품 팝업 띄우기)
        findViewById<Button>(R.id.button_english).setOnClickListener {
            popupBurgerSelectionContainer.visibility = View.VISIBLE
        }

        // 세트-단품 팝업
        findViewById<LinearLayout>(R.id.button_set).setOnClickListener {
            val intent = Intent(
                this@ActualPracticePlaceSelectionActivity,
                ActualPracticeQRSuccess::class.java
            )
            startActivity(intent)
            popupBurgerSelectionContainer.visibility = View.GONE
        }

        findViewById<LinearLayout>(R.id.button_single).setOnClickListener {
            popupSingBurgerContainer.visibility = View.VISIBLE
            popupBurgerSelectionContainer.visibility = View.GONE
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            popupBurgerSelectionContainer.visibility = View.GONE
        }

        // 단품 팝업
        // 수량 변경 버튼
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

        // 단품 장바구니 추가
        findViewById<Button>(R.id.button_add_to_cart).setOnClickListener {
            val intent = Intent(
                this@ActualPracticePlaceSelectionActivity,
                ActualPracticeActivity::class.java
            )
            startActivity(intent)
            popupSingBurgerContainer.visibility = View.GONE
        }

    }
}