package com.example.kiumi;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActualPracticeBurgerSetOrderActivity : AppCompatActivity() {
    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_burger_set_order)

        val quantityText: TextView = findViewById(R.id.quantity)

        findViewById<Button>(R.id.button_add_to_cart).setOnClickListener {
            val intent = Intent(this, ActualPracticeMainActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, ActualPracticeMainActivity::class.java)
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
    }
}
