package dev.upaya.kasina

import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FlashLight @Inject constructor(
    @ApplicationContext context: Context,
) {

    private var isOn = false

    private val cameraManager = context.getSystemService(CAMERA_SERVICE) as CameraManager
    private val cameraId = cameraManager.cameraIdList.first { id ->
        cameraManager.getCameraCharacteristics(id)[CameraCharacteristics.FLASH_INFO_AVAILABLE] == true
    }

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun turnOn() {

        if (isOn) return

        isOn = true

        cameraManager.setTorchMode(cameraId, isOn)
        firebaseAnalytics.logEvent("flashlight_on") { }
    }

    fun turnOff() {

        if (!isOn) return

        isOn = false

        cameraManager.setTorchMode(cameraId, isOn)
        firebaseAnalytics.logEvent("flashlight_off") { }
    }
}
