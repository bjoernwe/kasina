package dev.upaya.kasina.flashlight

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.upaya.kasina.inputkeys.InputKeyHandler
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FlashLightViewModel @Inject constructor(
    app: Application,
    private val inputKeyHandler: InputKeyHandler,
    private val flashLightStateControllerResource: FlashLightStateControllerResource,
) : AndroidViewModel(app) {

    init {
        addCloseable(flashLightStateControllerResource)
        flashLightStateControllerResource.start(context = app, scope = viewModelScope)
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
        flashLightStateControllerResource.turnOff()
    }
}
