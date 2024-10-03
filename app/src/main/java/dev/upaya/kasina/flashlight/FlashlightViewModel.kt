package dev.upaya.kasina.flashlight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.upaya.kasina.data.FlashlightEventsRepository
import dev.upaya.kasina.data.Session
import dev.upaya.kasina.inputkeys.InputKeyHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FlashlightViewModel @Inject constructor(
    private val flashlight: Flashlight,
    private val inputKeyHandler: InputKeyHandler,
    private val flashlightStateController: FlashlightStateController,
    private val flashlightEventsRepository: FlashlightEventsRepository,
) : ViewModel() {

    val isFlashlightOn = flashlightStateController.isFlashlightOn
    val recentSessions: Flow<List<Session>> = flashlightEventsRepository.recentSessions

    init {
        viewModelScope.launch {
            flashlightStateController.startControllingFlashlightState()
        }
        viewModelScope.launch {
            flashlight.events.collect { flashlightEventsRepository.storeEvent(it) }
        }
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
        flashlightStateController.turnOff()
    }
}
