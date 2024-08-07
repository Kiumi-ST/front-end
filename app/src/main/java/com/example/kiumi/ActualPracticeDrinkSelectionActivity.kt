package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActualPracticeDrinkSelectionActivity : AppCompatActivity() {

    private lateinit var menuItem: MenuItem
    private var isLargeSet: Boolean = false
    private lateinit var selectedSide: MenuItem
    private lateinit var selectedDrink: MenuItem

    private val drinkMenuItems = listOf(
        MenuItem("코카-콜라", "0", "143 Kcal", R.drawable.coca_cola, false),
        MenuItem("스프라이트", "0", "149 Kcal", R.drawable.sprite, false),
        MenuItem("코카 콜라 제로", "0", "0 Kcal", R.drawable.coca_cola, false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_drink_selection)

        menuItem = intent.getParcelableExtra("menuItem") ?: return
        isLargeSet = intent.getBooleanExtra("isLargeSet", false)
        selectedSide = intent.getParcelableExtra("selectedSide") ?: return

        val baseSetPrice = menuItem.price.replace("₩", "").replace(",", "").toInt()
        val setPrice = if (isLargeSet) baseSetPrice + 2700 else baseSetPrice + 2000

        findViewById<TextView>(R.id.title).text = "${menuItem.name} - ${if (isLargeSet) "라지세트" else "세트"}"
        findViewById<TextView>(R.id.title2).text = "₩$setPrice ${menuItem.calories}"
        findViewById<TextView>(R.id.menu_name_text).text = "${menuItem.name}"

        findViewById<LinearLayout>(R.id.button_drink1).setOnClickListener {
            selectedDrink = drinkMenuItems[0]
            goToBurgerSetOrder()
        }

        findViewById<LinearLayout>(R.id.button_drink2).setOnClickListener {
            selectedDrink = drinkMenuItems[1]
            goToBurgerSetOrder()
        }

        findViewById<LinearLayout>(R.id.button_drink3).setOnClickListener {
            selectedDrink = drinkMenuItems[2]
            goToBurgerSetOrder()
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, ActualPracticeMainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToBurgerSetOrder() {
        val intent = Intent(this, ActualPracticeBurgerSetOrderActivity::class.java).apply {
            putExtra("menuItem", menuItem)
            putExtra("isLargeSet", isLargeSet)
            putExtra("selectedSide", selectedSide)
            putExtra("selectedDrink", selectedDrink)
        }
        startActivity(intent)
    }
}