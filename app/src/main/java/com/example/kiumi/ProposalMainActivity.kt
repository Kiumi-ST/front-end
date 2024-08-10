package com.example.kiumi

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.kiumi.databinding.ActivityProposalMainBinding
import com.google.firebase.analytics.FirebaseAnalytics

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
    private lateinit var notificationBadge: TextView
    private lateinit var priceTextView: TextView
    private var previousActivity: String? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProposalMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이전 액티비티 이름을 인텐트로부터 받아오기
        previousActivity = intent.getStringExtra("previous_activity")

        home = findViewById(R.id.home)
        burger = findViewById(R.id.burger)
        side = findViewById(R.id.side)
        drink = findViewById(R.id.drink)

        homeText = findViewById(R.id.home_text)
        burgerText = findViewById(R.id.burger_text)
        sideText = findViewById(R.id.side_text)
        drinkText = findViewById(R.id.drink_text)

        notificationBadge = findViewById(R.id.notification_badge)
        priceTextView = findViewById(R.id.price)

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
        findViewById<TextView>(R.id.order_history).setOnClickListener {
            val intent = Intent(this@ProposalMainActivity, ProposalOrderActivity::class.java)
            startActivity(intent)
        }

        CartManager.addListener { updateOrderSummary() }
        updateOrderSummary()

        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    // 뒤로 가기 버튼을 눌렀을 때 실행되는 콜백 메소드
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            val params = Bundle().apply {
                putString("previous_screen_name", previousActivity) // 잘못 클릭했던 화면 이름
                putString("screen_name", "개선안_메인") // 현재 화면 이름
            }
            firebaseAnalytics.logEvent("go_back", params)

            // 실제로 뒤로 가기 동작을 수행하도록 추가
            isEnabled = false // 콜백을 비활성화하여 기본 뒤로 가기 동작을 수행
            onBackPressedDispatcher.onBackPressed() // 기본 뒤로 가기 동작 수행
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
        val title: TextView = findViewById(R.id.title)
        val singleImage: ImageView = findViewById(R.id.burger_single_image)
        val singlePriceCalories: TextView = findViewById(R.id.burger_price)

        title.text = menuItem.name
        singleImage.setImageResource(menuItem.imageResourceId)
        singlePriceCalories.text = "${menuItem.price}"

        popupBurgerSelectionContainer.visibility = View.VISIBLE
        popupSingBurgerContainer.bringToFront()

        // 세트-단품 팝업
        findViewById<LinearLayout>(R.id.button_set).setOnClickListener {
            val intent = Intent(
                this@ProposalMainActivity,
                ProposalSetSelectionActivity::class.java
            ).apply {
                putExtra("menuItem", menuItem)
                putExtra("previous_activity", "개선안_버거 선택 (세트, 단품 여부)")
            }
            startActivity(intent)
            popupBurgerSelectionContainer.visibility = View.GONE
        }

        findViewById<LinearLayout>(R.id.button_single).setOnClickListener { // 뒤로 가기가 안 됨.
            popupBurgerSelectionContainer.visibility = View.GONE
            showSingleItemPopup(menuItem)
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            popupBurgerSelectionContainer.visibility = View.GONE
        }
    }

    fun showSingleItemPopup(menuItem: ProposalMenuItem) {
        val popupSingleBurger: RelativeLayout = findViewById(R.id.popup_single_burger)
        val singleImage: ImageView = findViewById(R.id.single_image)
        val singleTitle: TextView = findViewById(R.id.single_title)
        val singlePriceCalories: TextView = findViewById(R.id.single_price_calories)
        var quantity = 1
        val quantityText: TextView = findViewById(R.id.quantity)

        singleImage.setImageResource(menuItem.imageResourceId)
        singleTitle.text = menuItem.name
        singlePriceCalories.text = "${menuItem.price}"

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

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            popupSingleBurger.visibility = View.GONE
        }

        // 장바구니 추가
        findViewById<LinearLayout>(R.id.button_add_to_cart).setOnClickListener {
            val orderItem = ProposalOrderItem(menuItem, quantity)
            ProposalCartManager.addItem(orderItem)
            popupSingleBurger.visibility = View.GONE
            updateOrderSummary()
            val intent = Intent(
                this@ProposalMainActivity,
                ProposalCartAddedActivity::class.java
            ).apply {
                putExtra("ITEM_PRICE", menuItem.price)
                putExtra("ITEM_QUANTITY", quantity)
                putExtra("previous_activity", "개선안_단품")
            }
            startActivity(intent)
        }
    }

    private fun updateOrderSummary() {
        val totalItems = ProposalCartManager.getTotalItems()
        val totalPrice = ProposalCartManager.getTotalPrice()

        notificationBadge.text = totalItems.toString()
        priceTextView.text = totalPrice
    }
}


