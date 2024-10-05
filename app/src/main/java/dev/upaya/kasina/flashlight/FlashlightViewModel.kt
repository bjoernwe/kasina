package dev.upaya.kasina.flashlight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.upaya.kasina.data.SessionStateRepository
import dev.upaya.kasina.inputkeys.InputKeyHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FlashlightViewModel @Inject constructor(
    private val inputKeyHandler: InputKeyHandler,
    private val flashlightStateController: FlashlightStateController,
    private val sessionStateRepository: SessionStateRepository,
) : ViewModel() {

    val sessionState = sessionStateRepository.sessionState
    val recentSessions = sessionStateRepository.recentSessions
    val currentSession = sessionStateRepository.currentSession

    init {
        viewModelScope.launch {
            flashlightStateController.startControllingFlashlightState()
        }
        viewModelScope.launch {
            sessionStateRepository.startStoringSessions()
        }
    }

    fun handleButtonPress(scope: CoroutineScope) {
        inputKeyHandler.handleButtonPress(scope = scope)
    }

    fun handleButtonRelease() {
        inputKeyHandler.handleButtonRelease()
    }

    fun turnFlashOff() {
        flashlightStateController.turnOff()
    }
}
