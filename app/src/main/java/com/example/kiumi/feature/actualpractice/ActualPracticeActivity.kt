package com.example.kiumi.feature.actualpractice

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import android.Manifest
import com.example.kiumi.data.CartManager
import com.example.kiumi.feature.common.PhotoCaptureService
import com.example.kiumi.data.PointManager
import com.example.kiumi.feature.common.PopupActivity
import com.example.kiumi.R

class ActualPracticeActivity : PopupActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var startTime: Long = 0
    private var endTime: Long = 0
    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice)

        CartManager.clearCart()
        PointManager.resetPoint()

        // Obtain the FirebaseAnalytics instance
        firebaseAnalytics = Firebase.analytics

        // 카메라 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // 권한이 부여되지 않은 경우, 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // 권한이 부여된 경우, 서비스 시작
            startPhotoCaptureService()
        }

        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            firebaseAnalytics.logEvent("QR_code_scan"){
                param(FirebaseAnalytics.Param.CONTENT, "QR_code_scan")
            }

            PointManager.setPointEarned(true)
            val intent = Intent(
                this@ActualPracticeActivity,
                ActualPracticeQRSuccess::class.java
            ).apply { putExtra("previous_activity", "실전 연습_대기") }
            startActivity(intent)
        }

        // 주문하기 버튼 클릭 시
        findViewById<Button>(R.id.button_order).setOnClickListener {
            firebaseAnalytics.logEvent("button_click_order"){
                param(FirebaseAnalytics.Param.CONTENT, "order")
            }
            val intent = Intent(
                this@ActualPracticeActivity,
                ActualPracticePlaceSelectionActivity::class.java
            ).apply { putExtra("previous_activity", "실전 연습_대기") }
            startActivity(intent)
        }

        // 언어 버튼 클릭 시
        findViewById<Button>(R.id.button_language).setOnClickListener {
            val intent = Intent(
                this@ActualPracticeActivity,
                ActualPracticeLanguageActivity::class.java
            ).apply { putExtra("previous_activity", "실전 연습_대기") }
            startActivity(intent)
        }

        // 도움 버튼 클릭 시
        findViewById<Button>(R.id.button_help).setOnClickListener {
        }
        
        // 뒤로 가기를 onBackPressedDispatcher를 통해 등록
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    // 뒤로 가기 버튼을 눌렀을 때 실행되는 콜백 메소드
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로 가기 실행 시 실행할 동작 코드 구현
            val params = Bundle().apply {
                putString("previous_screen_name", "연습 방식 선택") // 잘못 클릭했던 화면 이름
                putString("screen_name", "실전 연습_대기") // 현재 화면 이름
            }
            firebaseAnalytics.logEvent("go_back", params)

            // 실제로 뒤로 가기 동작을 수행하도록 추가
            isEnabled = false // 콜백을 비활성화하여 기본 뒤로 가기 동작을 수행
            onBackPressedDispatcher.onBackPressed() // 기본 뒤로 가기 동작 수행
        }
    }

    override fun onStart() {
        super.onStart()
        startTime = System.currentTimeMillis()
    }

    override fun onStop() {
        super.onStop()
        endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        val params = Bundle().apply {
            putLong("screen_duration", duration)
            putString("screen_name", "실전 연습_대기")
        }
        firebaseAnalytics.logEvent("screen_view_duration", params)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우, 서비스 시작
                startPhotoCaptureService()
            } else {
                // 권한이 거부된 경우, 사용자에게 안내
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startPhotoCaptureService() {
        // 권한이 허용된 후 서비스 시작
        val serviceIntent = Intent(this, PhotoCaptureService::class.java)
        serviceIntent.putExtra("ACTIVITY_NAME", this::class.java.simpleName)
        startService(serviceIntent)
    }


}