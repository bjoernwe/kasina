package dev.upaya.kasina.flashlight

import dev.upaya.kasina.inputkeys.InputKeyHandler
import dev.upaya.kasina.inputkeys.PressableKeyState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FlashLightStateController @Inject constructor(
    private val flashLight: FlashLight,
    private val inputKeyHandler: InputKeyHandler,
) {

    val isFlashLightOn = flashLight.isOn

    private var _flashLightState = MutableStateFlow(FlashLightState.OFF)

    private var flashLightStateJob: Job? = null
    private var flashLightOnOffJob: Job? = null

    fun start(scope: CoroutineScope) {
        flashLightStateJob = launchFlashLightStateJob(scope)
        flashLightOnOffJob = launchFlashLightOnOffJob(scope)
    }

    fun turnOff() {
        _flashLightState.update { FlashLightState.OFF }
    }

    private fun launchFlashLightStateJob(scope: CoroutineScope) = scope.launch {
        inputKeyHandler.volumeKeysState.collect { keyEvent ->
            updateFlashLightState(keyEvent)
        }
    }

    private fun launchFlashLightOnOffJob(scope: CoroutineScope) = scope.launch {
        _flashLightState.collect { state ->
            when (state) {
                FlashLightState.OFF -> flashLight.turnOff()
                else -> flashLight.turnOn()
            }
        }
    }

    private fun updateFlashLightState(keyEvent: PressableKeyState) {
        _flashLightState.update { currentState ->
            when (currentState) {
                FlashLightState.OFF -> handleKeyForOffState(keyEvent) ?: currentState
                FlashLightState.TRANSITION_TO_ON -> handleKeyForTransitionToOnState(keyEvent) ?: currentState
                FlashLightState.TRANSITION_TO_OFF -> handleKeyForTransitionToOffState(keyEvent) ?: currentState
                FlashLightState.ON_SWITCHED -> handleKeyForSwitchedOnState(keyEvent) ?: currentState
                FlashLightState.ON_HOLDING -> handleKeyForHoldingOnState(keyEvent) ?: currentState
            }
        }
    }
}


private fun handleKeyForOffState(keyEvent: PressableKeyState): FlashLightState? {
    return when (keyEvent) {
        PressableKeyState.PRESSED -> FlashLightState.TRANSITION_TO_ON
        else -> null
    }
}

private fun handleKeyForTransitionToOnState(keyEvent: PressableKeyState): FlashLightState? {
    return when (keyEvent) {
        PressableKeyState.RELEASED -> FlashLightState.ON_SWITCHED
        PressableKeyState.PRESSED_LONG -> FlashLightState.ON_HOLDING
        else -> null
    }
}

private fun handleKeyForTransitionToOffState(keyEvent: PressableKeyState): FlashLightState? {
    return when (keyEvent) {
        PressableKeyState.RELEASED -> FlashLightState.OFF
        PressableKeyState.PRESSED_LONG -> FlashLightState.ON_HOLDING
        else -> null
    }
}

private fun handleKeyForSwitchedOnState(keyEvent: PressableKeyState): FlashLightState? {
    return when (keyEvent) {
        PressableKeyState.PRESSED -> FlashLightState.TRANSITION_TO_OFF
        else -> null
    }
}

private fun handleKeyForHoldingOnState(keyEvent: PressableKeyState): FlashLightState? {
    return when (keyEvent) {
        PressableKeyState.RELEASED -> FlashLightState.OFF
        else -> null
    }
}
