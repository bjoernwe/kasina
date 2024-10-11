package dev.upaya.kasina.flashlight

import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Flashlight @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val _events = MutableStateFlow(false)
    val events: StateFlow<Boolean> = _events

    private val turnedOn: Boolean
        get() = _events.value
    private val turnedOff: Boolean
        get() = !turnedOn

    private val cameraManager = context.getSystemService(CAMERA_SERVICE) as CameraManager
    private val cameraId = cameraManager.cameraIdList.first { id ->
        cameraManager.getCameraCharacteristics(id)[CameraCharacteristics.FLASH_INFO_AVAILABLE] == true
    }
    private val torchCallback = object : CameraManager.TorchCallback() {
        override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
            if (events.value == enabled)
                return
            _events.value = enabled
        }
    }

    init {
        cameraManager.registerTorchCallback(torchCallback, null)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun turnOn(dispatcher: CoroutineDispatcher = Dispatchers.IO) {

        if (turnedOn)
            return

        GlobalScope.launch(dispatcher) {
            cameraManager.setTorchMode(cameraId, true)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun turnOff(dispatcher: CoroutineDispatcher = Dispatchers.IO) {

        if (turnedOff)
            return

        GlobalScope.launch(dispatcher) {
            cameraManager.setTorchMode(cameraId, false)
        }
    }
}
