package com.test.kiumi.feature.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.test.kiumi.R
import com.test.kiumi.feature.tutorial.TutorialActivity

abstract class PopupActivity : AppCompatActivity() {
    private lateinit var difficultyReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // BroadcastReceiver 초기화
        difficultyReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // isDifficult가 true인 경우 팝업 표시
                showPopup()
            }
        }
        // BroadcastReceiver 등록
        val filter = IntentFilter("com.example.kiumi.ACTION_DIFFICULTY_DETECTED")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(difficultyReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(difficultyReceiver, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // BroadcastReceiver 해제
        unregisterReceiver(difficultyReceiver)
    }

    protected fun showPopup() {
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.diff_popup_layout, null)

        val popupWindow = PopupWindow(popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true)

        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.TOP or Gravity.END, 50, 200)

        // 팝업 클릭 시 순서지침 화면으로 이동
        popupView.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
            popupWindow.dismiss() // 팝업 창 닫기
        }

        popupView.postDelayed({
            popupWindow.dismiss()
        }, 3000)
    }
}
