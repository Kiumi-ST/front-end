package com.example.kiumi

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeTutorialFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_tutorial, container, false)

        // TextView에 애니메이션 설정
        val burgerSetButton2: TextView = view.findViewById(R.id.burger_set_2)
        burgerSetButton2.setBackgroundResource(R.drawable.blinking_border_animation)
        val animationDrawable = burgerSetButton2.background as AnimationDrawable
        animationDrawable.start()

        val drinksButton2: TextView = view.findViewById(R.id.drinks_2)

        burgerSetButton2.setOnClickListener {
            (activity as TutorialMainActivity).replaceFragment(BurgerTutorialFragment())
        }

        drinksButton2.setOnClickListener {
            (activity as TutorialMainActivity).replaceFragment(DrinkFragment())
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeTutorialFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
