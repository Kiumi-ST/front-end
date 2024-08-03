package com.example.kiumi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProposalQRSuccess : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_proposal_qrsuccess)

        // 확인 버튼을 ID로 찾기
        val buttonConfirm: Button = findViewById(R.id.buttonConfirm)

        // 버튼에 클릭 리스너 설정
        buttonConfirm.setOnClickListener {
            // ActualPracticePaymentSelection 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, ProposalPaymentSelection::class.java)
            startActivity(intent)
        }
    }
}