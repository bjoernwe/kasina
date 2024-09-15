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
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FlashLight @Inject constructor(
    @ApplicationContext context: Context,
) : AutoCloseable {

    private var isOn = false

    private val cameraManager = context.getSystemService(CAMERA_SERVICE) as CameraManager
    private val cameraId = cameraManager.cameraIdList.first { id ->
        cameraManager.getCameraCharacteristics(id)[CameraCharacteristics.FLASH_INFO_AVAILABLE] == true
    }

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    init {
        cameraManager.registerTorchCallback(object : CameraManager.TorchCallback() {
            override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                isOn = enabled
            }
        }, null)
    }

    override fun close() {
        turnOff()
    }

    private fun turnOn() {

        if (isOn)
            return

        cameraManager.setTorchMode(cameraId, true)
        firebaseAnalytics.logEvent("flashlight_on") { }
    }

    private fun turnOff() {

        if (!isOn)
            return

        cameraManager.setTorchMode(cameraId, false)
        firebaseAnalytics.logEvent("flashlight_off") { }
    }

    suspend fun turnOnFor(timeMillis: Long) {

        if (isOn)
            return

        turnOn()
        try {
            delay(timeMillis)
        } finally {
            turnOff()
        }
    }
}
