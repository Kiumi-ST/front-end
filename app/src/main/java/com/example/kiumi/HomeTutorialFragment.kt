package com.example.kiumi

import android.content.Context
import android.content.SharedPreferences
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

    private lateinit var preferences: SharedPreferences
    private lateinit var burgerSetButton2: TextView
    private lateinit var drinksButton2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = activity?.getSharedPreferences("com.example.kiumi.PREFERENCES", Context.MODE_PRIVATE)!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_tutorial, container, false)

        // View 초기화
        burgerSetButton2 = view.findViewById(R.id.burger_set_2)
        drinksButton2 = view.findViewById(R.id.drinks_2)

        // 버튼 상태 설정 및 클릭 리스너 설정
        burgerSetButton2.setOnClickListener {
            // 프래그먼트를 BurgerTutorialFragment로 교체
            (activity as TutorialMainActivity).replaceFragment(BurgerTutorialFragment())
        }

        drinksButton2.setOnClickListener {
            (activity as TutorialMainActivity).replaceFragment(DrinkFragment())
        }

        // 초기 버튼 상태 설정
        updateBurgerSetButtonState()

        return view
    }

    override fun onResume() {
        super.onResume()
        updateBurgerSetButtonState()
    }

    private fun updateBurgerSetButtonState() {
        val burgerSetClicked = preferences.getBoolean("burger_set_clicked", false)
        if (burgerSetClicked) {
            burgerSetButton2.setBackgroundResource(R.drawable.button_background_gray)
        } else {
            burgerSetButton2.setBackgroundResource(R.drawable.blinking_border_animation)
            val animationDrawable = burgerSetButton2.background as AnimationDrawable
            animationDrawable.start()
        }
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
