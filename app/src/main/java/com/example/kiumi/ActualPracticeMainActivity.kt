package com.example.kiumi

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.kiumi.databinding.ActivityActualPracticeMainBinding
import com.example.kiumi.databinding.ActivitySurveyBinding

class ActualPracticeMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityActualPracticeMainBinding
    private lateinit var home: LinearLayout
    private lateinit var burger: LinearLayout
    private lateinit var side: LinearLayout
    private lateinit var drink: LinearLayout
    private lateinit var homeText: TextView
    private lateinit var burgerText: TextView
    private lateinit var sideText: TextView
    private lateinit var drinkText: TextView
    private var selectedIndicator: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualPracticeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        home = findViewById(R.id.home)
        burger = findViewById(R.id.burger)
        side = findViewById(R.id.side)
        drink = findViewById(R.id.drink)

        homeText = findViewById(R.id.home_text)
        burgerText = findViewById(R.id.burger_text)
        sideText = findViewById(R.id.side_text)
        drinkText = findViewById(R.id.drink_text)

        setMenuClickListener(home, homeText)
        setMenuClickListener(burger, burgerText)
        setMenuClickListener(side, sideText)
        setMenuClickListener(drink, drinkText)

        // Default selection
        selectMenu(home, homeText)
    }

    private fun setMenuClickListener(menu: LinearLayout, menuText: TextView) {
        menu.setOnClickListener {
            selectMenu(menu, menuText)
            // Update the right content based on the selection
            updateContent(menu.id)
        }
    }

    private fun selectMenu(selectedMenu: LinearLayout, selectedText: TextView) {
        resetMenuStyles()
        selectedText.setTypeface(selectedText.typeface, Typeface.BOLD)
        selectedMenu.addView(createSelectedIndicator())
    }

    private fun resetMenuStyles() {
        homeText.setTypeface(Typeface.DEFAULT)
        burgerText.setTypeface(Typeface.DEFAULT)
        sideText.setTypeface(Typeface.DEFAULT)
        drinkText.setTypeface(Typeface.DEFAULT)

        removeSelectedIndicator(home)
        removeSelectedIndicator(burger)
        removeSelectedIndicator(side)
        removeSelectedIndicator(drink)
    }

    private fun removeSelectedIndicator(menu: LinearLayout) {
        if (menu.childCount > 2) {
            menu.removeViewAt(menu.childCount - 1)
        }
    }

    private fun createSelectedIndicator(): View {
        if (selectedIndicator == null) {
            selectedIndicator = View(this)
            selectedIndicator!!.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 5)
            selectedIndicator!!.setBackgroundResource(R.drawable.selected_menu_item)
        }
        return selectedIndicator as View
    }

    private fun updateContent(selectedMenuId: Int) {
        // Update the right content based on the selected menu
        // You can add your logic here to update the content
    }
}