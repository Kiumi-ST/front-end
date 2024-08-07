package com.example.kiumi;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActualPracticeBurgerSetOrderActivity : AppCompatActivity() {
    private lateinit var menuItem: MenuItem
    private var isLargeSet: Boolean = false
    private lateinit var selectedSide: MenuItem
    private lateinit var selectedDrink: MenuItem
    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_burger_set_order)

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
