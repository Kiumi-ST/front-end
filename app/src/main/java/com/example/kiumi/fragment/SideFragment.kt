package com.example.kiumi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kiumi.adapter.MenuAdapter
import com.example.kiumi.R
import com.example.kiumi.data.MenuItem
import com.example.kiumi.feature.actualpractice.ActualPracticeMainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SideFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_side, container, false)

        //리사이클러 뷰
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val menuItems = listOf(
//            MenuItem("츄러스", "₩1,500", "145 Kcal", R.drawable.churros, false),
//            MenuItem("츄러스 & 선데이 - 콤보", "₩3,000", " ", R.drawable.churros_sundae_combo, true),
//            MenuItem("케이준 비프 스낵랩", "₩2,200", "292 Kcal", R.drawable.cajun_beef_snack_wrap, false),
//            MenuItem("맥너겟® - 6조각", "₩3,000", "256 Kcal", R.drawable.mcnuggets, false),
//            MenuItem("후렌치 후라이 - 미디엄", "₩1,500", "332 Kcal", R.drawable.french_fries_medium, false),
//            MenuItem("코울슬로", "₩1,900", "179 Kcal", R.drawable.coleslaw, true)
            MenuItem("츄러스", "₩1,500", "145 Kcal", R.drawable.churros, false),
            MenuItem("아이스크림 츄러스", "₩3,000", " ", R.drawable.icecream_churro, true),
            MenuItem("비프 스낵랩", "₩2,200", "292 Kcal", R.drawable.beef_wrap, false),
            MenuItem("치킨 너겟 - 6조각", "₩3,000", "256 Kcal", R.drawable.chicken_nuggets, false),
            MenuItem("후렌치 후라이 - 미디엄", "₩1,500", "332 Kcal", R.drawable.french_fries_medium, false),
            MenuItem("코울슬로", "₩1,900", "179 Kcal", R.drawable.coleslaw, true)
        )

        val adapter = MenuAdapter(menuItems) { menuItem ->
            (activity as? ActualPracticeMainActivity)?.showSingleItemPopup(menuItem)
        }
        recyclerView.adapter = adapter

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SideFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}