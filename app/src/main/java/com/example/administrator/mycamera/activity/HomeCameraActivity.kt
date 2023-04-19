package com.example.administrator.mycamera.activity

import android.os.Bundle
import android.view.OrientationEventListener
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity

class HomeCameraActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initData()

    }

    private fun initData(){
//        val cameraProcessFuture = ProcessCameraProvider.getInstance(this)
//        cameraProcessFuture.addListener(Runnable {
//            val cameraProvider = cameraProcessFuture.get()
//
//            // By default, the use cases set their target rotation to match the
//            // display’s rotation.
//            val preview = buildPreview()
//            val imageAnalysis = buildImageAnalysis()
//            val imageCapture = buildImageCapture()
//
//            cameraProvider.bindToLifecycle(
//                this, cameraSelector, preview, imageAnalysis, imageCapture)
//        }, mainExecutor)
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

//                imageAnalysis.targetRotation = rotation
//                imageCapture.targetRotation = rotation
            }
        }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

}