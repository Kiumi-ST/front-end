package com.example.kiumi

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kiumi.databinding.ActivityTutorialMainBinding
import java.util.*

class TutorialMainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    lateinit var binding: ActivityTutorialMainBinding
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
    private lateinit var orderHistoryTextView: TextView
    private lateinit var preferences: SharedPreferences
    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false // 기본값 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences 초기화
        preferences = getSharedPreferences("com.example.kiumi.PREFERENCES", MODE_PRIVATE)

        // 이전 액티비티에서 전달된 isTTSActive 값 받아오기 (기본값 false)
        isTTSActive = intent.getBooleanExtra("isTTSActive", false)

        // TTS 초기화
        tts = TextToSpeech(this, this)

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

        orderHistoryTextView = findViewById(R.id.order_history)

        setMenuClickListener(home, homeText, homeIndicator, HomeTutorialFragment())
        setMenuClickListener(burger, burgerText, burgerIndicator, BurgerTutorialFragment())
        setMenuClickListener(side, sideText, sideIndicator, SideFragment())
        setMenuClickListener(drink, drinkText, drinkIndicator, DrinkFragment())

        // Default selection
        selectMenu(home, homeText, homeIndicator)
        replaceFragment(HomeTutorialFragment())

        // QR 코드 버튼 초기화
        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
        }

        findViewById<TextView>(R.id.order_history).setOnClickListener {
            val fragment = OrderSummaryDialogTutorialFragment()

            // Bundle에 isTTSActive 값을 담아 전달
            val bundle = Bundle()
            bundle.putBoolean("isTTSActive", isTTSActive)
            fragment.arguments = bundle

            fragment.show(supportFragmentManager, "OrderSummaryDialogTutorial")

            // 팝업 호출 시 orderHistoryTextView 배경을 button_background_gray로 변경
            orderHistoryTextView.setBackgroundResource(R.drawable.button_background_gray)
        }

        // 주문 내역 버튼 초기 배경 설정
        updateOrderHistoryButtonState()
    }

    override fun onResume() {
        super.onResume()
        updateOrderHistoryButtonState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            // 팝업에서 돌아올 때 배경을 blinking_border_animation으로 변경하고 애니메이션 시작
            orderHistoryTextView.setBackgroundResource(R.drawable.blinking_border_animation)
            val orderHistoryAnimation = orderHistoryTextView.background as AnimationDrawable
            orderHistoryAnimation.start()
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

        // If the fragment is BurgerTutorialFragment, speak the instruction
        // TTS가 활성화된 상태이면서 MainActivity에서 TTS를 사용하지 않으려면 조건을 추가
        val isTTSActiveForMainActivity = intent.getBooleanExtra("isTTSActiveForMainActivity", true)
        if (fragment is BurgerTutorialFragment && isTTSActive && isTTSActiveForMainActivity) {
            speakText("창녕 갈릭 버거를 클릭해주세요")
        }
    }

    private fun updateOrderHistoryButtonState() {
        val burgerSetClicked = preferences.getBoolean("burger_set_clicked", false)

        // TextView 참조 가져오기
        val priceTextView: TextView = findViewById(R.id.price)

        if (burgerSetClicked) {
            orderHistoryTextView.setBackgroundResource(R.drawable.blinking_border_animation)
            val orderHistoryAnimation = orderHistoryTextView.background as AnimationDrawable
            orderHistoryAnimation.start()

            // burgerSetClicked가 true이면 텍스트를 ₩8600으로 변경
            priceTextView.text = "₩8600"
        } else {
            orderHistoryTextView.setBackgroundResource(R.drawable.button_background_gray)

            // burgerSetClicked가 false이면 텍스트를 ₩0으로 초기화
            priceTextView.text = "₩0"
        }
    }

    fun showBurgerSelectionPopup(menuItem: MenuItem) {
        val popupBurgerSelectionContainer: RelativeLayout = findViewById(R.id.popup_burger_selection)
        val popupSingBurgerContainer: RelativeLayout = findViewById(R.id.popup_single_burger)

        var quantity = 1
        val quantityText: TextView = findViewById(R.id.quantity)

        popupBurgerSelectionContainer.visibility = View.VISIBLE
        popupSingBurgerContainer.bringToFront()

        val takeOutLayout: LinearLayout = findViewById(R.id.button_set)
        takeOutLayout.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = takeOutLayout.background as AnimationDrawable
        animationDrawable.start()

        // 세트-단품 팝업
        findViewById<LinearLayout>(R.id.button_set).setOnClickListener {
            val intent = Intent(
                this@TutorialMainActivity,
                TutorialSetSelectionActivity::class.java
            )
            intent.putExtra("isTTSActive", isTTSActive)  // isTTSActive 값을 전달
            startActivity(intent)
            popupBurgerSelectionContainer.visibility = View.GONE
        }

        // button_single 클릭 리스너 설정 (비활성화)
        findViewById<LinearLayout>(R.id.button_single).setOnClickListener {
            // 이 버튼은 클릭해도 아무런 동작을 하지 않도록 합니다.
        }

        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            popupBurgerSelectionContainer.visibility = View.GONE
        }

        // 단품 팝업의 다른 설정은 그대로 유지
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
                this@TutorialMainActivity,
                TutorialMainActivity::class.java
            )
            startActivity(intent)
            popupSingBurgerContainer.visibility = View.GONE
        }

        // TTS로 안내 메시지 출력
        if (isTTSActive) {
            speakText("세트 선택을 클릭해주세요")
        }
    }


    fun showDrinkSelectionPopup(menuItem: MenuItem) {
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
                this@TutorialMainActivity,
                TutorialMainActivity::class.java
            )
            startActivity(intent)

        }
    }

    fun showSideSelectionPopup(menuItem: MenuItem) {
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
                this@TutorialMainActivity,
                TutorialMainActivity::class.java
            )
            startActivity(intent)

        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA) {
                Log.e("TTS", "언어 데이터가 없습니다.")
            } else if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "이 언어는 지원되지 않습니다.")
            } else if (isTTSActive) {
                //speakText("버거와 세트 버튼을 클릭해주세요")
            }
        } else {
            Log.e("TTS", "TTS 초기화 실패")
        }
    }

    private fun speakText(text: String) {
        if (isTTSActive) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            Log.d("TTS", "음성 출력: $text")
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}

