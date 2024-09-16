package dev.upaya.kasina

import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent


class FlashLightResource(context: Context) : AutoCloseable {

    private var _isOn = false
    val isOn: Boolean
        get() = _isOn
    private val isOff: Boolean
        get() = !isOn

    private val cameraManager = context.getSystemService(CAMERA_SERVICE) as CameraManager
    private val cameraId = cameraManager.cameraIdList.first { id ->
        cameraManager.getCameraCharacteristics(id)[CameraCharacteristics.FLASH_INFO_AVAILABLE] == true
    }
    private val torchCallback = object : CameraManager.TorchCallback() {
        override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
            _isOn = enabled
        }
    }

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    init {
        cameraManager.registerTorchCallback(torchCallback, null)
    }

    @Override
    override fun close() {
        turnOff()
        cameraManager.unregisterTorchCallback(torchCallback)
    }

    fun turnOn() {

        if (_isOn)
            return

        cameraManager.setTorchMode(cameraId, true)
        firebaseAnalytics.logEvent("flashlight_on") { }
    }

    fun turnOff() {

        if (isOff)
            return

        cameraManager.setTorchMode(cameraId, false)
        firebaseAnalytics.logEvent("flashlight_off") { }
    }

    fun toggle() {
        if (isOn) {
            turnOff()
        } else {
            turnOn()
        }
    }
}
