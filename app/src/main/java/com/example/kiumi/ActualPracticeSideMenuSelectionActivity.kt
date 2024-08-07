package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActualPracticeSideMenuSelectionActivity : AppCompatActivity() {
    private lateinit var menuItem: MenuItem
    private var isLargeSet: Boolean = false
    private lateinit var selectedSide: MenuItem

    private val sideMenuItems = listOf(
        MenuItem("후렌치 후라이 - 미디엄", "₩0", "332 Kcal", R.drawable.french_fries_medium, false),
        MenuItem("코울슬로", "₩200", "179 Kcal", R.drawable.coleslaw, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_side_menu_selection)

        menuItem = intent.getParcelableExtra("menuItem") ?: return
        isLargeSet = intent.getBooleanExtra("isLargeSet", false)

        val baseSetPrice = menuItem.price.replace("₩", "").replace(",", "").toInt()
        val setPrice = if (isLargeSet) baseSetPrice + 2700 else baseSetPrice + 2000

        findViewById<TextView>(R.id.title).text = "${menuItem.name} - ${if (isLargeSet) "라지세트" else "세트"}"
        findViewById<TextView>(R.id.title2).text = "₩$setPrice ${menuItem.calories}"
        findViewById<TextView>(R.id.menu_name_text).text = "${menuItem.name}"

        findViewById<LinearLayout>(R.id.button_side1).setOnClickListener {
            selectedSide = sideMenuItems[0]
            goToDrinkSelection()
        }

        findViewById<LinearLayout>(R.id.button_side2).setOnClickListener {
            selectedSide = sideMenuItems[1]
            goToDrinkSelection()
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            val intent = Intent(this, ActualPracticeMainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToDrinkSelection() {
        val intent = Intent(this, ActualPracticeDrinkSelectionActivity::class.java).apply {
            putExtra("menuItem", menuItem)
            putExtra("isLargeSet", isLargeSet)
            putExtra("selectedSide", selectedSide)
        }
        startActivity(intent)
    }
}