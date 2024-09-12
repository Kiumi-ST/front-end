package com.example.kiumi

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class OrderSummaryDialogTutorialFragment : DialogFragment(), TextToSpeech.OnInitListener {

    private lateinit var adapter: MenuAdapter
    private lateinit var tts: TextToSpeech
    private var isTTSActive: Boolean = false

    val menuItems = listOf(
        MenuItem("후렌치 후라이 - 미디엄", "₩1,500", "332 Kcal", R.drawable.french_fries_medium, false),
        MenuItem("비프 스낵랩", "₩2,200", "292 Kcal", R.drawable.beef_wrap, false),
        MenuItem("아이스크림 츄러스", "₩3,000", " ", R.drawable.icecream_churro, true),
        MenuItem("더블업 치즈버거", "₩5,600", "619 Kcal", R.drawable.double_up_cheeseburger, false),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bundle로 전달된 isTTSActive 값 받기
        arguments?.let {
            isTTSActive = it.getBoolean("isTTSActive", false)  // 기본값 false로 설정
        }

        // TTS 초기화
        tts = TextToSpeech(activity, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_summary_dialog_tutorial, container, false)

        // 리사이클러 뷰 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = MenuAdapter(menuItems) { }
        recyclerView.adapter = adapter

        // 선택안함 버튼 클릭 시
        val confirmButton: Button = view.findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            // 팝업 닫기 전에 SharedPreferences에 애니메이션 상태 저장
            val sharedPref = activity?.getSharedPreferences("com.example.kiumi.PREFERENCES", Context.MODE_PRIVATE)
            sharedPref?.edit()?.putBoolean("blinkingAnimation", true)?.apply()

            val intent = Intent(activity, TutorialOrderActivity::class.java).apply {
                putExtra("isTTSActive", isTTSActive)  // 다음 액티비티로 isTTSActive 값 전달
            }
            activity?.startActivity(intent)
            dismiss()
        }

        // Button에 애니메이션 설정
        confirmButton.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = confirmButton.background as AnimationDrawable
        animationDrawable.start()

        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // 프래그먼트가 닫힐 때 TutorialMainActivity에 알림을 보냄
        (activity as? TutorialMainActivity)?.onFragmentClosed()
    }

    override fun onResume() {
        super.onResume()
        // 프래그먼트가 화면에 표시되면 TTS로 안내 메시지 출력
        if (isTTSActive && ::tts.isInitialized) {
            speakText("선택안함을 클릭해주세요")
        }
    }

    private fun speakText(text: String) {
        if (isTTSActive) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.KOREAN)
            // TTS가 활성화된 경우 초기화 후 메시지 출력
            if (isTTSActive) {
                speakText("선택안함을 클릭해주세요")
            }
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
