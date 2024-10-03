package com.test.kiumi.feature.common

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.Manifest
import com.test.kiumi.data.AnalyzeResponse
import com.test.kiumi.data.RetrofitClient

class PhotoCaptureService : LifecycleService() {

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var handler: Handler
    private lateinit var captureRunnable: Runnable
    private val captureInterval: Long = 5000 // 5초 간격
    private var activityName: String? = null

    override fun onCreate() {
        super.onCreate()

        cameraExecutor = Executors.newSingleThreadExecutor()
        handler = Handler(Looper.getMainLooper())

        // 카메라를 사용하기 전에 권한이 부여되었는지 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용된 경우에만 카메라를 시작
            startCamera()
        } else {
            // 권한이 없는 경우, 로그를 출력하고 서비스 종료
            Log.e("PhotoCaptureService", "카메라 권한이 없습니다.")
            stopSelf()
        }

        captureRunnable = object : Runnable {
            override fun run() {
                captureAndSendImage()
                handler.postDelayed(this, captureInterval)
            }
        }

        handler.post(captureRunnable)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Activity 이름 가져오기
        activityName = intent?.getStringExtra("ACTIVITY_NAME") ?: "Unknown"
        Log.d("PhotoCaptureService", "Received activity name: $activityName")
        // 서비스의 기본 동작을 유지하기 위해 super.onStartCommand 호출
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            // CameraProvider에 ImageCapture UseCase 바인딩
            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,              // LifecycleOwner (보통 Activity 또는 Fragment)
                    cameraSelector,    // 사용할 카메라 선택
                    imageCapture       // 사용하려는 UseCase
                )

            } catch (exc: Exception) {
                Log.e("CameraX", "카메라 바인딩 실패", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureAndSendImage() {
        if (::imageCapture.isInitialized) {
            val file = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")

            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
            imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.d("CameraX", "이미지 저장: ${file.absolutePath}")
                        sendImageToServer(file)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("CameraX", "이미지 캡처 실패", exception)
                    }
                })
        } else {
            Log.e("CameraX", "imageCapture is not initialized")
        }
    }

    private fun sendImageToServer(file: File) {
        // Retrofit을 사용해 이미지를 서버로 전송하는 로직 구현
        Log.d("Server", "이미지를 서버로 전송 중: ${file.absolutePath}")

        val screenName = activityName ?: "Unknown"
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val screenNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), screenName)

        RetrofitClient.flaskApiService.uploadImage(body, screenNameBody).enqueue(object :
            Callback<AnalyzeResponse> {
            override fun onResponse(call: Call<AnalyzeResponse>, response: Response<AnalyzeResponse>) {
                if (response.isSuccessful) {
                    val analyzeResponse = response.body()
                    Log.d("Server Response", "Dominant Emotion: ${analyzeResponse?.dominantEmotion}, Is Difficult: ${analyzeResponse?.isDifficult}")
                    // isDifficult가 true일 경우 Broadcast 전송
                    if (analyzeResponse?.isDifficult == true) {
//                        Toast.makeText(
//                            this@PhotoCaptureService,
//                            "isDifficult가 true임(서버 연결 확인용, 나중에 지워야 함)",
//                            Toast.LENGTH_LONG
//                        ).show()
                        val intent = Intent("com.example.kiumi.ACTION_DIFFICULTY_DETECTED")
                        sendBroadcast(intent)
                    }
                } else {
                    Log.e("Server Response", "응답 오류: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AnalyzeResponse>, t: Throwable) {
                Log.e("Server Response", "서버 통신 실패: ${t.message}")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(captureRunnable)
        cameraExecutor.shutdown()
    }
}