package com.example.kiumi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BurgerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BurgerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: MenuAdapter
    val menuItems = listOf(
        MenuItem("창녕 갈릭 버거", "₩6,600", "674 Kcal", R.drawable.changneung_gallic_burger, true),
        MenuItem("트리플 치즈버거", "₩5,600", "619 Kcal", R.drawable.triple_cheese_burger, false),
        MenuItem("불고기 버거", "₩2,200", "430 Kcal", R.drawable.bulgogi_burger, false),
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
        val view = inflater.inflate(R.layout.fragment_burger, container, false)

        val btnAll: Button = view.findViewById(R.id.btn_all)
        val btnBeef: Button = view.findViewById(R.id.btn_beef)
        val btnChicken: Button = view.findViewById(R.id.btn_chicken)
        val btnSeafood: Button = view.findViewById(R.id.btn_seafood)
        val btnBulgogi: Button = view.findViewById(R.id.btn_bulgogi)

        val buttons = listOf(btnAll, btnBeef, btnChicken, btnSeafood, btnBulgogi)

        buttons.forEach { button ->
            button.setOnClickListener {
                buttons.forEach { it.isSelected = false }
                button.isSelected = true
                filterMenuItems(button.id)
            }
        }

        //리사이클러 뷰
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = MenuAdapter(menuItems)
        recyclerView.adapter = adapter

        // Inflate the layout for this fragment
        return view
    }

    private fun filterMenuItems(buttonId: Int) {
        val filteredItems = when (buttonId) {
            R.id.btn_beef -> menuItems.filter { it.name.contains("비프") }
            R.id.btn_chicken -> menuItems.filter { it.name.contains("치킨") }
            R.id.btn_seafood -> menuItems.filter { it.name.contains("시푸드") }
            R.id.btn_bulgogi -> menuItems.filter { it.name.contains("불고기") }
            else -> menuItems
        }
        adapter.updateMenuItems(filteredItems)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BurgerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BurgerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}