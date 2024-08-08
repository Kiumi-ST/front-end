package com.example.kiumi

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kiumi.databinding.ActivityProposalMainBinding

class ProposalMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityProposalMainBinding
    private lateinit var home: LinearLayout
    private lateinit var burger: LinearLayout
    private lateinit var side: LinearLayout
    private lateinit var drink: LinearLayout
    private lateinit var homeText: TextView
    private lateinit var burgerText: TextView
    private lateinit var sideText: TextView
    private lateinit var drinkText: TextView
    private lateinit var homeIndicator: View
    private lateinit var burgerIndicator: View
    private lateinit var sideIndicator: View
    private lateinit var drinkIndicator: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProposalMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        home = findViewById(R.id.home)
        burger = findViewById(R.id.burger)
        side = findViewById(R.id.side)
        drink = findViewById(R.id.drink)

        homeText = findViewById(R.id.home_text)
        burgerText = findViewById(R.id.burger_text)
        sideText = findViewById(R.id.side_text)
        drinkText = findViewById(R.id.drink_text)

        homeIndicator = findViewById(R.id.home_indicator)
        burgerIndicator = findViewById(R.id.burger_indicator)
        sideIndicator = findViewById(R.id.side_indicator)
        drinkIndicator = findViewById(R.id.drink_indicator)

        setMenuClickListener(home, homeText, homeIndicator, ProposalHomeFragment())
        setMenuClickListener(burger, burgerText, burgerIndicator, ProposalBurgerFragment())
        setMenuClickListener(side, sideText, sideIndicator, ProposalSideFragment())
        setMenuClickListener(drink, drinkText, drinkIndicator, ProposalDrinkFragment())

        // Default selection
        selectMenu(home, homeText, homeIndicator)
        replaceFragment(ProposalHomeFragment())

        // QR 코드 버튼 초기화
        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            val intent = Intent(this@ProposalMainActivity, ProposalQRSuccess::class.java)
            startActivity(intent)
        }

        // 주문 내역 버튼 클릭 시
        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            val intent = Intent(this@ProposalMainActivity, ProposalOrderActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setMenuClickListener(menu: LinearLayout, menuText: TextView, menuIndicator: View, fragment: Fragment) {
        menu.setOnClickListener {
            selectMenu(menu, menuText, menuIndicator)
            replaceFragment(fragment)
        }
    }

    private fun selectMenu(selectedMenu: LinearLayout, selectedText: TextView, selectedIndicator: View) {
        resetMenuStyles()
        selectedText.setTypeface(selectedText.typeface, Typeface.BOLD)
        selectedIndicator.visibility = View.VISIBLE
    }

    private fun resetMenuStyles() {
        homeText.setTypeface(Typeface.DEFAULT)
        burgerText.setTypeface(Typeface.DEFAULT)
        sideText.setTypeface(Typeface.DEFAULT)
        drinkText.setTypeface(Typeface.DEFAULT)

        homeIndicator.visibility = View.GONE
        burgerIndicator.visibility = View.GONE
        sideIndicator.visibility = View.GONE
        drinkIndicator.visibility = View.GONE
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.menu_section, fragment)
            .commit()
    }

    fun showBurgerSelectionPopup(menuItem: ProposalMenuItem) {
        val popupBurgerSelectionContainer: RelativeLayout = findViewById(R.id.popup_burger_selection)
        val popupSingBurgerContainer: RelativeLayout = findViewById(R.id.popup_single_burger)

        var quantity = 1
        val quantityText: TextView = findViewById(R.id.quantity)


        popupBurgerSelectionContainer.visibility = View.VISIBLE
        popupSingBurgerContainer.bringToFront()

        // 세트-단품 팝업
        findViewById<LinearLayout>(R.id.button_set).setOnClickListener {
            val intent = Intent(
                this@ProposalMainActivity,
                ProposalSetSelectionActivity::class.java
            )
            startActivity(intent)
            popupBurgerSelectionContainer.visibility = View.GONE
        }

        findViewById<LinearLayout>(R.id.button_single).setOnClickListener {
            popupSingBurgerContainer.visibility = View.VISIBLE
            popupSingBurgerContainer.bringToFront()
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
        findViewById<LinearLayout>(R.id.button_add_to_cart).setOnClickListener {
            val intent = Intent(
                this@ProposalMainActivity,
                ProposalMainActivity::class.java
            )
            startActivity(intent)
            popupSingBurgerContainer.visibility = View.GONE
        }
    }

    fun showDrinkSelectionPopup(menuItem: ProposalMenuItem) {
        val popupSingleBurger: RelativeLayout = findViewById(R.id.popup_single_burger)
        var quantity = 1
        val quantityText: TextView = findViewById(R.id.quantity)

        popupSingleBurger.visibility = View.VISIBLE
        popupSingleBurger.bringToFront()

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
        findViewById<LinearLayout>(R.id.button_add_to_cart).setOnClickListener {
            val intent = Intent(
                this@ProposalMainActivity,
                ProposalMainActivity::class.java
            )
            startActivity(intent)
        }
    }

    fun showSideSelectionPopup(menuItem: ProposalMenuItem) {
        val popupSingleBurger: RelativeLayout = findViewById(R.id.popup_single_burger)
        var quantity = 1
        val quantityText: TextView = findViewById(R.id.quantity)

        popupSingleBurger.visibility = View.VISIBLE
        popupSingleBurger.bringToFront()

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
                this@ProposalMainActivity,
                ProposalMainActivity::class.java
            )
            startActivity(intent)
        }
    }
}


