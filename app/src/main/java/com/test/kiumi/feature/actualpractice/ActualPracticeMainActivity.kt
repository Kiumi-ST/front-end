package com.test.kiumi.feature.actualpractice

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.test.kiumi.fragment.BurgerFragment
import com.test.kiumi.data.CartManager
import com.test.kiumi.fragment.DrinkFragment
import com.test.kiumi.fragment.HomeFragment
import com.test.kiumi.data.MenuItem
import com.test.kiumi.data.OrderItem
import com.test.kiumi.fragment.OrderSummaryDialogFragment
import com.test.kiumi.feature.common.PhotoCaptureService
import com.test.kiumi.data.PointManager
import com.test.kiumi.feature.common.PopupActivity
import com.test.kiumi.R
import com.test.kiumi.fragment.SideFragment
import com.test.kiumi.databinding.ActivityActualPracticeMainBinding
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class ActualPracticeMainActivity : PopupActivity() {
    lateinit var binding: ActivityActualPracticeMainBinding
    private lateinit var home: LinearLayout
    private lateinit var burger: LinearLayout
    private lateinit var side: LinearLayout
    private lateinit var drink: LinearLayout
    private lateinit var homeText: TextView
    private lateinit var burgerText: TextView
    private lateinit var sideText: TextView
    private lateinit var drinkText: TextView
    private lateinit var notificationBadge: TextView
    private lateinit var priceTextView: TextView
    private lateinit var homeIndicator: View
    private lateinit var burgerIndicator: View
    private lateinit var sideIndicator: View
    private lateinit var drinkIndicator: View
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var previousActivity: String? = null
    private var popupStartTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualPracticeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

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

        homeIndicator = findViewById(R.id.home_indicator)
        burgerIndicator = findViewById(R.id.burger_indicator)
        sideIndicator = findViewById(R.id.side_indicator)
        drinkIndicator = findViewById(R.id.drink_indicator)

        notificationBadge = findViewById(R.id.notification_badge)
        priceTextView = findViewById(R.id.price)

        setMenuClickListener(home, homeText, homeIndicator, HomeFragment())
        setMenuClickListener(burger, burgerText, burgerIndicator, BurgerFragment())
        setMenuClickListener(side, sideText, sideIndicator, SideFragment())
        setMenuClickListener(drink, drinkText, drinkIndicator, DrinkFragment())

        // Default selection
        selectMenu(home, homeText, homeIndicator)
        replaceFragment(HomeFragment())

        // QR 코드 버튼 초기화
        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            PointManager.setPointEarned(true)
            val intent = Intent(this@ActualPracticeMainActivity, ActualPracticeQRSuccess::class.java)
                .apply { putExtra("previous_activity", "실전 연습_메인") }
            startActivity(intent)
        }

        // 주문 내역 버튼 클릭 시 팝업 호출
        findViewById<TextView>(R.id.order_history).setOnClickListener {
            OrderSummaryDialogFragment().show(supportFragmentManager, "OrderSummaryDialog")
        }

        // 처음으로 버튼 클릭 시
        findViewById<TextView>(R.id.gotohome).setOnClickListener {
            val intent = Intent(this@ActualPracticeMainActivity, ActualPracticeOrderCancelActivity::class.java)
                .apply { putExtra("previous_activity", "실전 연습_메인") }
            startActivity(intent)
        }

        CartManager.addListener { updateOrderSummary() }
        updateOrderSummary()
        
        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    // 뒤로 가기 버튼을 눌렀을 때 실행되는 콜백 메소드
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        var menuItemIsBurger: Boolean = false

        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            val popupBurgerSelectionContainer: RelativeLayout = findViewById(R.id.popup_burger_selection)
            val popupSingleBurger: RelativeLayout = findViewById(R.id.popup_single_burger)

            if (popupSingleBurger.visibility == View.VISIBLE){
                logPopupDuration("실전 연습_단품")
                popupSingleBurger.visibility = View.GONE

                if (menuItemIsBurger) {
                    val params = Bundle().apply {
                        putString("previous_screen_name", "실전 연습_버거 선택 (세트, 단품 여부)") // 잘못 클릭했던 화면 이름
                        putString("screen_name", "실전 연습_단품") // 현재 화면 이름
                    }
                    firebaseAnalytics.logEvent("go_back", params)
                    popupBurgerSelectionContainer.visibility = View.VISIBLE
                }else{
                    val params = Bundle().apply {
                        putString("previous_screen_name", "실전 연습_메인") // 잘못 클릭했던 화면 이름
                        putString("screen_name", "실전 연습_단품") // 현재 화면 이름
                    }
                    firebaseAnalytics.logEvent("go_back", params)
                }
                return
            }

            if (popupBurgerSelectionContainer.visibility == View.VISIBLE) {
                logPopupDuration("실전 연습_버거 선택 (세트, 단품 여부)")
                popupBurgerSelectionContainer.visibility = View.GONE

                val params = Bundle().apply {
                    putString("previous_screen_name", "실전 연습_메인") // 잘못 클릭했던 화면 이름
                    putString("screen_name", "실전 연습_버거 선택 (세트, 단품 여부)") // 현재 화면 이름
                }
                firebaseAnalytics.logEvent("go_back", params)
                return
            }

            val params = Bundle().apply {
                putString("previous_screen_name", previousActivity) // 잘못 클릭했던 화면 이름
                putString("screen_name", "실전 연습_메인") // 현재 화면 이름
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

    fun showBurgerSelectionPopup(menuItem: MenuItem) {
        val popupBurgerSelectionContainer: RelativeLayout = findViewById(R.id.popup_burger_selection)
        val popupSingBurgerContainer: RelativeLayout = findViewById(R.id.popup_single_burger)
        val title: TextView = findViewById(R.id.title)
        val singleImage: ImageView = findViewById(R.id.burger_single_image)
        val singlePriceCalories: TextView = findViewById(R.id.burger_price_calorie)

        title.text = menuItem.name
        singleImage.setImageResource(menuItem.imageResourceId)
        singlePriceCalories.text = "${menuItem.price} ${menuItem.calories}"

        // 팝업 열림 시간 기록
        recordPopupStartTime()

        popupBurgerSelectionContainer.visibility = View.VISIBLE
        popupSingBurgerContainer.bringToFront()

        // 세트-단품 팝업
        findViewById<LinearLayout>(R.id.button_set).setOnClickListener {
            val intent = Intent(
                this@ActualPracticeMainActivity,
                ActualPracticeSetSelectionActivity::class.java
            ).apply { 
              putExtra("menuItem", menuItem)
              putExtra("previous_activity", "실전 연습_버거 선택 (세트, 단품 여부)")
            }
            // 팝업 닫힘 시간 기록 및 로그
            logPopupDuration("실전 연습_버거 선택 (세트, 단품 여부)")
            startActivity(intent)
            popupBurgerSelectionContainer.visibility = View.GONE
        }

        findViewById<LinearLayout>(R.id.button_single).setOnClickListener { // 뒤로 가기가 안 됨.
            // 팝업 닫힘 시간 기록 및 로그
            logPopupDuration("실전 연습_버거 선택 (세트, 단품 여부)")
            popupBurgerSelectionContainer.visibility = View.GONE
            showSingleItemPopup(menuItem, showModifyButton = true)
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            // 팝업 닫힘 시간 기록 및 로그
            logPopupDuration("실전 연습_버거 선택 (세트, 단품 여부)")
            popupBurgerSelectionContainer.visibility = View.GONE
        }
    }

    fun showSingleItemPopup(menuItem: MenuItem, showModifyButton: Boolean = false) {
        val popupSingleBurger: RelativeLayout = findViewById(R.id.popup_single_burger)
        val singleImage: ImageView = findViewById(R.id.single_image)
        val singleTitle: TextView = findViewById(R.id.single_title)
        val singlePriceCalories: TextView = findViewById(R.id.single_price_calories)
        var quantity = 1
        val quantityText: TextView = findViewById(R.id.quantity)

        singleImage.setImageResource(menuItem.imageResourceId)
        singleTitle.text = menuItem.name
        singlePriceCalories.text = "${menuItem.price} ${menuItem.calories}"

        // 팝업 열림 시간 기록
        recordPopupStartTime()

        popupSingleBurger.visibility = View.VISIBLE
        popupSingleBurger.bringToFront()

        findViewById<Button>(R.id.button_nutrition_info).setOnClickListener {
            val intent = Intent(
                this@ActualPracticeMainActivity,
                ActualPracticeNutritionInfoActivity::class.java
            ).apply {
                putExtra("ITEM_NAME", menuItem.name)
                putExtra("ITEM_IMAGERESID", menuItem.imageResourceId)
                putExtra("previous_activity", "실전 연습_단품")
            }
            startActivity(intent)
        }

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
            // 팝업 닫힘 시간 기록 및 로그
            logPopupDuration("실전 연습_단품")
        }

        if (showModifyButton) {
            findViewById<Button>(R.id.button_add_modify).visibility = View.VISIBLE
            findViewById<Button>(R.id.button_add_modify).setOnClickListener {
                val intent = Intent(this, ActualPracticeBurgerCustomizationActivity::class.java)
                    .apply {
                        putExtra("menuItem", menuItem)
                        putExtra("previous_activity", "실전 연습_단품")
                    }
                startActivity(intent)
            }
        } else {
            findViewById<Button>(R.id.button_add_modify).visibility = View.GONE
        }

        // 장바구니 추가
        findViewById<Button>(R.id.button_add_to_cart).setOnClickListener {
            val orderItem = OrderItem(menuItem, quantity)
            CartManager.addItem(orderItem)
            popupSingleBurger.visibility = View.GONE
            updateOrderSummary()
            // 팝업 닫힘 시간 기록 및 로그
            logPopupDuration("실전 연습_단품")
            val intent = Intent(
                this@ActualPracticeMainActivity,
                ActualPracticeCartAddedActivity::class.java
            ).apply {
                putExtra("ITEM_PRICE", menuItem.price)
                putExtra("ITEM_QUANTITY", quantity)
                putExtra("previous_activity", "실전 연습_단품")
            }
            startActivity(intent)
        }

        // 뒤로 가기 버튼 처리
        onBackPressedCallback.isEnabled = true
        onBackPressedCallback.menuItemIsBurger = menuItem.name.endsWith("버거")
    }

    private fun updateOrderSummary() {
        val totalItems = CartManager.getTotalItems()
        val totalPrice = CartManager.getTotalPrice()

        notificationBadge.text = totalItems.toString()
        priceTextView.text = totalPrice
    }

    private fun notifyServiceOfCurrentActivity() {
        val serviceIntent = Intent(this, PhotoCaptureService::class.java)
        serviceIntent.putExtra("ACTIVITY_NAME", this::class.java.simpleName)
        // 이미 실행 중이면 onStartCommand만 호출됨
        startService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()
        notifyServiceOfCurrentActivity()
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
            putString("screen_name", "실전 연습_메인")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

    // 팝업이 열릴 때 호출
    private fun recordPopupStartTime(){
        popupStartTime = System.currentTimeMillis()
    }

    // 팝업이 닫힐 때 호출
    private fun logPopupDuration(popupName: String){
        val popupEndTime = System.currentTimeMillis()
        val duration = popupEndTime - popupStartTime

        val params = Bundle().apply {
            putString("screen_name", popupName) // 이것들 다 맞추기
            putLong("screen_duration", duration)
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

}
