package dev.upaya.kasina.flashlight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.upaya.kasina.inputkeys.InputKeyHandler
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


@HiltViewModel
class FlashLightViewModel @Inject constructor(
    private val inputKeyHandler: InputKeyHandler,
    private val flashLightStateController: FlashLightStateController,
) : ViewModel() {

    val isFlashLightOn = flashLightStateController.isFlashLightOn

    init {
        flashLightStateController.start(scope = viewModelScope)
    }

    fun handleVolumeDownPress(scope: CoroutineScope) {
        inputKeyHandler.handleVolumeDownPress(scope = scope)
    }

    fun handleVolumeUpPress(scope: CoroutineScope) {
        inputKeyHandler.handleVolumeUpPress(scope = scope)
    }

    fun handleVolumeDownRelease() {
        inputKeyHandler.handleVolumeDownRelease()
    }

    fun handleVolumeUpRelease() {
        inputKeyHandler.handleVolumeUpRelease()
    }

    fun turnFlashOff() {
        flashLightStateController.turnOff()
    }
}
