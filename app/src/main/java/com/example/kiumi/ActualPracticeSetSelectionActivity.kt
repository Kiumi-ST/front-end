package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActualPracticeSetSelectionActivity : AppCompatActivity() {

    private lateinit var menuItem: MenuItem
    private var isLargeSet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_set_selection)

        menuItem = intent.getParcelableExtra("menuItem") ?: return

        val titleTextView: TextView = findViewById(R.id.title)
        val setTextView: TextView = findViewById(R.id.button_set_text_title)
        val largeSetTextView: TextView = findViewById(R.id.button_large_set_text_title)
        val setPriceTextView: TextView = findViewById(R.id.button_set_text_price_calories)
        val largerSetPriceTextView: TextView = findViewById(R.id.button_large_set_text_price_calories)

        val baseSetPrice = menuItem.price.replace("₩", "").replace(",", "").toInt()

        titleTextView.text = "${menuItem.name}"
        setTextView.text = "${menuItem.name} - 세트"
        largeSetTextView.text = "${menuItem.name} - 라지세트"
        setPriceTextView.text = "₩${baseSetPrice + 2000} ${menuItem.calories}"
        largerSetPriceTextView.text = "₩${baseSetPrice + 2700} ${menuItem.calories}"

        findViewById<LinearLayout>(R.id.button_set).setOnClickListener {
            isLargeSet = false
            goToSideMenuSelection()
        }

        findViewById<LinearLayout>(R.id.button_large_set).setOnClickListener {
            isLargeSet = true
            goToSideMenuSelection()
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, ActualPracticeMainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToSideMenuSelection() {
        val intent = Intent(this, ActualPracticeSideMenuSelectionActivity::class.java).apply {
            putExtra("menuItem", menuItem)
            putExtra("isLargeSet", isLargeSet)
        }
        startActivity(intent)
    }
}