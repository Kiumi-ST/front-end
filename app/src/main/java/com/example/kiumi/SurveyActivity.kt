package com.example.kiumi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.kiumi.databinding.ActivityHelpBinding
import com.example.kiumi.databinding.ActivitySurveyBinding
import kotlinx.coroutines.launch

class SurveyActivity : AppCompatActivity() {
    lateinit var binding: ActivitySurveyBinding
    private lateinit var ratingBar: RatingBar
    private lateinit var radioGroupQ2: RadioGroup
    private lateinit var radioGroupQ3: RadioGroup
    private lateinit var radioGroupQ4: RadioGroup
    private lateinit var editTextQ5: EditText
    private lateinit var btnFinish: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //툴바
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화

        val toolbarView = supportActionBar?.customView
        val homeIcon = toolbarView?.findViewById<ImageView>(R.id.home_icon)
        val titleView = toolbarView?.findViewById<TextView>(R.id.toolbar_title)

        titleView?.text = "개선점 설문조사"

        homeIcon?.setOnClickListener {
            // 홈 아이콘 클릭 시 동작 구현
        }

        ratingBar = findViewById(R.id.ratingBar)
        radioGroupQ2 = findViewById(R.id.radioGroupQ2)
        radioGroupQ3 = findViewById(R.id.radioGroupQ3)
        radioGroupQ4 = findViewById(R.id.radioGroupQ4)
        editTextQ5 = findViewById(R.id.editTextTextMultiLine)
        btnFinish = findViewById(R.id.btnfinish)

        btnFinish.setOnClickListener {
            submitSurvey()
        }
    }

    private fun submitSurvey() {
        val q1 = ratingBar.rating
        val q2 = findViewById<RadioButton>(radioGroupQ2.checkedRadioButtonId)?.text.toString()
        val q3 = findViewById<RadioButton>(radioGroupQ3.checkedRadioButtonId)?.text.toString()
        val q4 = findViewById<RadioButton>(radioGroupQ4.checkedRadioButtonId)?.text.toString()
        val q5 = editTextQ5.text.toString()

        val surveyData = SurveyData(q1, q2, q3, q4, q5)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.submitSurvey(surveyData)
                if (response.isSuccessful) {
                    val surveyResponse = response.body()
                    Toast.makeText(this@SurveyActivity, surveyResponse?.msg, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        this@SurveyActivity,
                        "제출 실패: ${response.errorBody()?.string()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SurveyActivity, "오류 발생: ${e.message}", Toast.LENGTH_LONG).show()
                Log.d("mobile","오류 발생: ${e.message}")
            }
        }
    }
}