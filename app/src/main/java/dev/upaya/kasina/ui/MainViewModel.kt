package dev.upaya.kasina.ui

import android.app.Application
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _flashOn = MutableStateFlow(false)
    val flashOn: StateFlow<Boolean> = _flashOn

    // TODO: Remove dependency on Application context
    private val cameraManager = application.getSystemService(CAMERA_SERVICE) as CameraManager
    private val cameraId = cameraManager.cameraIdList.first { id ->
        cameraManager.getCameraCharacteristics(id)[CameraCharacteristics.FLASH_INFO_AVAILABLE] == true
    }

    fun toggleFlashLight() {
        _flashOn.value = !_flashOn.value
        cameraManager.setTorchMode(cameraId, _flashOn.value)
    }
}
