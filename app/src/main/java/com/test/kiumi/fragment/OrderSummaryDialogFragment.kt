package com.test.kiumi.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.kiumi.adapter.MenuAdapter
import com.test.kiumi.R
import com.test.kiumi.data.MenuItem
import com.test.kiumi.feature.actualpractice.ActualPracticeMainActivity
import com.test.kiumi.feature.actualpractice.ActualPracticeOrderActivity
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

    private var startTime: Long = 0
    private var endTime: Long = 0

    private lateinit var adapter: MenuAdapter
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    val menuItems = listOf(
        MenuItem("후렌치 후라이 - 미디엄", "₩1,500", "332 Kcal", R.drawable.french_fries_medium, false),
        MenuItem("비프 스낵랩", "₩2,200", "292 Kcal", R.drawable.beef_wrap, false),
        MenuItem("아이스크림 츄러스", "₩3,000", " ", R.drawable.icecream_churro, true),
        MenuItem("더블업 치즈버거", "₩5,600", "619 Kcal", R.drawable.double_up_cheeseburger, false),
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

        startTime = System.currentTimeMillis()

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // 뒤로 가기 버튼이 눌렸을 때 처리
                onBackPressed()
                true
            } else {
                false
            }
        }

        return dialog
    }

    private fun onBackPressed() {

        val params = Bundle().apply {
            putString("previous_screen_name", "실전 연습_메인")
            putString("screen_name", "실전 연습_주문 내역 클릭 시")
        }
        firebaseAnalytics.logEvent("go_back", params)

        dismiss() // 팝업 닫기
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        // 다이얼로그 종료 시 이벤트 로깅
        endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // 팝업 닫힐 때 체류 시간을 로깅
        val params = Bundle().apply {
            putString("screen_name", "실전 연습_주문 내역 클릭 시")
            putLong("screen_duration", duration)
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
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