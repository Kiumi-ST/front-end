package com.example.kiumi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ActualPracticeCartAddedActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_added)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

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

        // 3초 후에 이동
        Toast.makeText(this, "3초 동안 화면이 유지됩니다", Toast.LENGTH_LONG).show()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(
                this@ActualPracticeCartAddedActivity,
                ActualPracticeMainActivity::class.java
            ).apply {
                putExtra("previous_activity", "실전 연습_장바구니 추가 완료")
            }
            startActivity(intent)
        }, 3000)
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
            putString("screen_name", "실전 연습_장바구니 추가 완료")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

}