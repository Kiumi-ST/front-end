package com.example.kiumi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kiumi.adapter.ProposalMenuAdapter
import com.example.kiumi.R
import com.example.kiumi.data.ProposalMenuItem
import com.example.kiumi.feature.proposal.ProposalMainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProposalSideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProposalSideFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_proposal_side, container, false)

        //리사이클러 뷰
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val menuItems = listOf(
//            ProposalMenuItem("츄러스", "₩1,500", R.drawable.churros, false),
//            ProposalMenuItem("츄러스 & 선데이 - 콤보", "₩3,000", R.drawable.churros_sundae_combo, true),
//            ProposalMenuItem("케이준 비프 스낵랩", "₩2,200", R.drawable.cajun_beef_snack_wrap, false),
//            ProposalMenuItem("맥너겟® - 6조각", "₩3,000", R.drawable.mcnuggets, false),

            ProposalMenuItem("츄러스", "₩1,500", R.drawable.churros, false),
            ProposalMenuItem("아이스크림 츄러스", "₩3,000", R.drawable.icecream_churro, true),
            ProposalMenuItem("비프 스낵랩", "₩2,200", R.drawable.beef_wrap, false),
            ProposalMenuItem("치킨 너겟 - 6조각", "₩3,000", R.drawable.chicken_nuggets, false)
        )

        val adapter = ProposalMenuAdapter(menuItems) { menuItem ->
            (activity as? ProposalMainActivity)?.showSingleItemPopup(menuItem)
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
         * @return A new instance of fragment ProposalSideFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProposalSideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}