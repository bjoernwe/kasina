package dev.upaya.kasina

import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FlashLight @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val cameraManager = context.getSystemService(CAMERA_SERVICE) as CameraManager
    private val cameraId = cameraManager.cameraIdList.first { id ->
        cameraManager.getCameraCharacteristics(id)[CameraCharacteristics.FLASH_INFO_AVAILABLE] == true
    }

    fun turnOn() {
        cameraManager.setTorchMode(cameraId, true)
    }

    fun turnOff() {
        cameraManager.setTorchMode(cameraId, false)
    }
}
