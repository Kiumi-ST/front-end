package com.example.kiumi.feature.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.kiumi.R
import com.example.kiumi.databinding.ActivityEnglishPracticeBinding
import java.util.*

class EnglishPracticeActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityEnglishPracticeBinding
    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnglishPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve isTTSActive value from SharedPreferences
        val sharedPref = getSharedPreferences("com.example.kiumi.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        isTTSActive = sharedPref.getBoolean("isTTSActive", false)

        // Initialize TextToSpeech
        tts = TextToSpeech(this, this)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.toolbar)

        val toolbarView = supportActionBar?.customView
        val homeIcon = toolbarView?.findViewById<ImageView>(R.id.home_icon)
        val titleView = toolbarView?.findViewById<TextView>(R.id.toolbar_title)
        val backIcon = toolbarView?.findViewById<ImageView>(R.id.back_icon)

        backIcon?.setOnClickListener {
            finish()
        }

        titleView?.text = "도움말"

        homeIcon?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val expressions = arrayOf(
            binding.soldout,
            binding.takeout,
            binding.waiting,
            binding.best,
            binding.creditcard,
            binding.order,
            binding.hot,
            binding.ice,
            binding.small,
            binding.medium,
            binding.large,
            binding.drink
        )

        val clickListener = View.OnClickListener { view ->
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.popup_expression_detail, null)
            dialogBuilder.setView(dialogView)

            val dialog = dialogBuilder.create()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()

            val xButton = dialogView.findViewById<ImageView>(R.id.cancelButton)
            val image = dialogView.findViewById<ImageView>(R.id.engExPopUpImage)
            val korean = dialogView.findViewById<TextView>(R.id.engExPopUpKorean)
            val english = dialogView.findViewById<TextView>(R.id.engExPopUpEnglish)
            val meaning = dialogView.findViewById<TextView>(R.id.engExPopUpMeaning)
            val description = dialogView.findViewById<TextView>(R.id.engExPopUpDescription)

            when (view.id) {
                R.id.takeout -> {
                    image.setImageResource(R.drawable.eng_ex_takeout)
                    image.scaleType = ImageView.ScaleType.CENTER
                    korean.text = "테이크 아웃"
                    english.text = "take out"
                    meaning.text = "뜻: 포장"
                    description.text = "이 버튼을 선택하면\n 메뉴를 매장에서 먹고 가는 게 아니라\n 밖으로 포장해 갈 수 \n있습니다."
                }
                R.id.waiting -> {
                    image.setImageResource(R.drawable.eng_ex_waiting)
                    image.scaleType = ImageView.ScaleType.CENTER
                    korean.text = "웨이팅"
                    english.text = "waiting"
                    meaning.text = "뜻: 대기하기"
                    description.text = "매장에 손님이 많아\n대기해야 할 경우,\n매장에 들어갈 수 있는\n대기 번호를 발급받을 수 있습니다."
                }
                R.id.best -> {
                    image.setImageResource(R.drawable.eng_ex_best)
                    image.scaleType = ImageView.ScaleType.CENTER
                    korean.text = "베스트"
                    english.text = "best"
                    meaning.text = "뜻: 인기 메뉴"
                    description.text = "일반적으로 맛있거나 \n가장 인기가 많은 음식을 가리킵니다."
                }
                R.id.creditcard -> {
                    image.setImageResource(R.drawable.eng_ex_creditcard)
                    image.scaleType = ImageView.ScaleType.CENTER
                    korean.text = "크레딧 카드"
                    english.text = "credit card"
                    meaning.text = "뜻: 신용 카드"
                    description.text = "신용카드 결제를 \n말합니다."
                }
                R.id.order -> {
                    image.setImageResource(R.drawable.eng_ex_order)
                    image.scaleType = ImageView.ScaleType.CENTER
                    korean.text = "오더"
                    english.text = "order"
                    meaning.text = "뜻: 주문하기"
                    description.text = "이 버튼을 선택하면\n메뉴를 선택하고 \n주문할 수 있습니다."
                }
                R.id.hot -> {
                    image.setImageResource(R.drawable.eng_ex_hot)
                    image.scaleType = ImageView.ScaleType.CENTER
                    korean.text = "핫"
                    english.text = "hot"
                    meaning.text = "뜻: 따뜻한 메뉴"
                    description.text = "뜨거운 음식이나\n 음료를 말합니다."
                }
                R.id.ice -> {
                    image.setImageResource(R.drawable.eng_ex_ice)
                    image.scaleType = ImageView.ScaleType.CENTER
                    korean.text = "아이스"
                    english.text = "ice"
                    meaning.text = "뜻: 시원한 메뉴"
                    description.text = "시원한 음식이나\n 음료를 말합니다."
                }
                R.id.small -> {
                    image.setImageResource(R.drawable.eng_ex_small)
                    image.scaleType = ImageView.ScaleType.CENTER

                    val paddingInDp = 37
                    val scale = resources.displayMetrics.density
                    val paddingInPx = (paddingInDp * scale + 0.5f).toInt()

                    image.setPadding(0, paddingInPx, 0, 0)

                    korean.text = "스몰"
                    english.text = "small"
                    meaning.text = "뜻: 작은 사이즈"
                    description.text = "작은 사이즈의 \n음식이나 음료를 \n나타냅니다."
                }
                R.id.medium -> {
                    image.setImageResource(R.drawable.eng_ex_medium)
                    image.scaleType = ImageView.ScaleType.CENTER
                    val paddingInDp = 18
                    val scale = resources.displayMetrics.density
                    val paddingInPx = (paddingInDp * scale + 0.5f).toInt()

                    image.setPadding(0, paddingInPx, 0, 0)
                    korean.text = "미디엄"
                    english.text = "medium"
                    meaning.text = "뜻: 중간 사이즈"
                    description.text = "중간 사이즈의 \n음식이나 음료를 \n말합니다."
                }
                R.id.large -> {
                    image.setImageResource(R.drawable.eng_ex_large)
                    image.scaleType = ImageView.ScaleType.CENTER
                    korean.text = "라지"
                    english.text = "large"
                    meaning.text = "뜻: 큰 사이즈"
                    description.text = "큰 사이즈의 \n음식이나 음료를 \n말합니다."
                }
                R.id.drink -> {
                    image.setImageResource(R.drawable.eng_ex_drink)
                    image.scaleType = ImageView.ScaleType.CENTER
                    korean.text = "드링크"
                    english.text = "drink"
                    meaning.text = "뜻: 음료"
                    description.text = "음료를 의미합니다."
                }
            }

            if (isTTSActive) {
                // Read the meaning and description with delays and skipping "뜻:"
                tts.playSilentUtterance(500, TextToSpeech.QUEUE_FLUSH, null) // 0.3-second delay before meaning
                val meaningText = meaning.text.toString().removePrefix("뜻: ")
                tts.speak(meaningText, TextToSpeech.QUEUE_ADD, null, null)
                tts.playSilentUtterance(1000, TextToSpeech.QUEUE_ADD, null) // 1-second delay before description
                tts.speak(description.text.toString(), TextToSpeech.QUEUE_ADD, null, null)
            }

            dialog.setOnDismissListener {
                tts.stop()
            }

            xButton.setOnClickListener {
                dialog.dismiss()
            }
        }

        for (ex in expressions) {
            ex.setOnClickListener(clickListener)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle the error
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
