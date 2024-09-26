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
            _flashLightState.update { currentState ->
                transitionToNextState(currentState, keyEvent)
            }
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
}


private fun transitionToNextState(currentState: FlashLightState, keyEvent: PressableKeyState): FlashLightState {
    return when (currentState to keyEvent) {
        FlashLightState.OFF               to PressableKeyState.PRESSED      -> FlashLightState.TRANSITION_TO_ON
        FlashLightState.TRANSITION_TO_ON  to PressableKeyState.PRESSED_LONG -> FlashLightState.ON_HOLDING
        FlashLightState.TRANSITION_TO_ON  to PressableKeyState.RELEASED     -> FlashLightState.ON_SWITCHED
        FlashLightState.ON_SWITCHED       to PressableKeyState.PRESSED      -> FlashLightState.TRANSITION_TO_OFF
        FlashLightState.TRANSITION_TO_OFF to PressableKeyState.PRESSED_LONG -> FlashLightState.ON_HOLDING
        FlashLightState.TRANSITION_TO_OFF to PressableKeyState.RELEASED     -> FlashLightState.OFF
        FlashLightState.ON_HOLDING        to PressableKeyState.RELEASED     -> FlashLightState.OFF
        else -> currentState
    }
}
