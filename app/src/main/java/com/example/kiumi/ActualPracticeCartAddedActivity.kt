package com.example.kiumi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ActualPracticeCartAddedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_added)

        val price: TextView = findViewById(R.id.price)

        // 인텐트에서 데이터를 받아옴
        val itemPrice = intent.getStringExtra("ITEM_PRICE")

        // 데이터 설정
        price.text = itemPrice
    }
}