package com.example.kiumi

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderSummaryDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderSummaryDialogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: MenuAdapter
    private lateinit var firebaseAnalytics: FirebaseAnalytics
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
        // FirebaseAnalytics 초기화
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_summary_dialog, container, false)

        //리사이클러 뷰
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = MenuAdapter(menuItems){ menuItem ->
            (activity as? ActualPracticeMainActivity)?.showSingleItemPopup(menuItem)
            dismiss()
        }
        recyclerView.adapter = adapter

        //선택안함 버튼 클릭 시
        val confirmButton: Button = view.findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            val intent = Intent(activity, ActualPracticeOrderActivity::class.java)
                .apply { putExtra("previous_activity", "실전 연습_주문 내역 클릭 시") }
            startActivity(intent)
            dismiss()
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // 뒤로 가기 또는 다이얼로그 종료 시 이벤트 로깅
        val params = Bundle().apply {
            putString("previous_screen_name", "실전 연습_메인") // 이전 화면 이름
            putString("screen_name", "실전 연습_주문 내역 클릭 시") // 현재 화면 이름
        }
        firebaseAnalytics.logEvent("go_back", params)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderSummaryDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderSummaryDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}