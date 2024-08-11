package com.example.kiumi

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OrderSummaryDialogTutorialFragment : DialogFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: MenuAdapter
    val menuItems = listOf(
        MenuItem("후렌치 후라이 - 미디엄", " ", "332 Kcal", R.drawable.french_fries_medium, false),
        MenuItem("케이준 비프 스낵랩", "₩2,200", "292 Kcal", R.drawable.cajun_beef_snack_wrap, false),
        MenuItem("츄러스 & 선데이 - 콤보", "₩3,000", " ", R.drawable.churros_sundae_combo, true),
        MenuItem("트리플 치즈버거", "₩5,600", "619 Kcal", R.drawable.triple_cheese_burger, false),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_summary_dialog_tutorial, container, false)

        // 리사이클러 뷰 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // 클릭 이벤트를 빈 리스너로 설정
        adapter = MenuAdapter(menuItems) {
            // 클릭해도 아무런 동작이 없도록 설정
        }
        recyclerView.adapter = adapter

        // 선택안함 버튼 클릭 시
        val confirmButton: Button = view.findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            val intent = Intent(activity, TutorialOrderActivity::class.java)
            startActivity(intent)
            dismiss()
        }

        // Button에 애니메이션 설정
        val confirmButtonAnimation: Button = view.findViewById(R.id.confirm_button)
        confirmButtonAnimation.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = confirmButtonAnimation.background as AnimationDrawable
        animationDrawable.start()

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderSummaryDialogTutorialFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
