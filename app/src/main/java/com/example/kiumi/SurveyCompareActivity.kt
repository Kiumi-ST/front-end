package com.example.kiumi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.kiumi.databinding.ActivitySurveyBinding
import com.example.kiumi.databinding.ActivitySurveyCompareBinding
import kotlinx.coroutines.launch

class SurveyCompareActivity : AppCompatActivity() {
    lateinit var binding: ActivitySurveyCompareBinding

    private lateinit var question1Group: RadioGroup
    private lateinit var question2Group: RadioGroup
    private lateinit var commentsEditText: EditText
    private lateinit var submitButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyCompareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //툴바
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.toolbar)

        val toolbarView = supportActionBar?.customView
        val homeIcon = toolbarView?.findViewById<ImageView>(R.id.home_icon)
        val titleView = toolbarView?.findViewById<TextView>(R.id.toolbar_title)
        val backIcon = toolbarView?.findViewById<ImageView>(R.id.back_icon)

        backIcon?.setOnClickListener {
            finish()
        }

        titleView?.text = "설문조사"

        homeIcon?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        question1Group = findViewById(R.id.check)
        question2Group = findViewById(R.id.comf)
        commentsEditText = findViewById(R.id.editTextTextMultiLine)
        submitButton = findViewById(R.id.btnfinish)

        submitButton.setOnClickListener {
            sendVotingData()
        }
    }

    private fun sendVotingData() {
        val question1Answer = findViewById<RadioButton>(question1Group.checkedRadioButtonId).text.toString()
        val question2Answer = findViewById<RadioButton>(question2Group.checkedRadioButtonId).text.toString()
        val comments = commentsEditText.text.toString()

        val votingRequest = VotingRequest(question1Answer, question2Answer, comments)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.springBootApiService.submitVotingResult(votingRequest)
                if (response.isSuccessful) {
                    val VotingResponse = response.body()
                    Toast.makeText(this@SurveyCompareActivity, VotingResponse?.msg, Toast.LENGTH_LONG).show()
                    val intent = Intent(
                        this@SurveyCompareActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@SurveyCompareActivity,
                        "제출 실패: ${response.errorBody()?.string()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SurveyCompareActivity, "오류 발생: ${e.message}", Toast.LENGTH_LONG).show()
                Log.d("mobile","오류 발생: ${e.message}")
            }
        }
    }

}