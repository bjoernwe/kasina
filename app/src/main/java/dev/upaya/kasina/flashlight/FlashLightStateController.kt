package dev.upaya.kasina.flashlight

import dev.upaya.kasina.inputkeys.InputKeyHandler
import dev.upaya.kasina.inputkeys.PressableKeyState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
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
        flashLightOnOffJob = launchFlashLightOnOffJob(scope)
        flashLightStateJob = launchFlashLightStateJob(scope)
    }

    fun turnOff() {
        _flashLightState.update { FlashLightState.OFF }
    }

    /**
     * This job subscribes to our own flashlight state and turns the actual flashlight on/off
     * accordingly.
     */
    private fun launchFlashLightOnOffJob(scope: CoroutineScope) = scope.launch {
        _flashLightState.collect { state ->
            when (state) {
                FlashLightState.OFF -> flashLight.turnOff()
                else -> flashLight.turnOn()
            }
        }
    }

    /**
     * This jobs keeps the flashlight state updated according to the incoming events.
     */
    private fun launchFlashLightStateJob(scope: CoroutineScope) = scope.launch {
        inputKeyHandler.volumeKeysState
            .combine(flashLight.isOn) { keyEvent, isFlashLightOn -> keyEvent to isFlashLightOn }
            .collect { keyAndFlash ->
                val (keyEvent, isFlashLightOn) = keyAndFlash
                updateFlashLightState(keyEvent, isFlashLightOn)
            }
    }

    private fun updateFlashLightState(keyEvent: PressableKeyState, isFlashLightOn: Boolean) {
        _flashLightState.update { currentState ->
            calcFlashlightState(currentState, keyEvent, isFlashLightOn)
        }
    }

}


private fun calcFlashlightState(currentState: FlashLightState, keyEvent: PressableKeyState, isFlashLightOn: Boolean): FlashLightState {

    // Did the user turn the flashlight on/off manually?
    if (!isStateCongruentWithFlashLight(currentState, isFlashLightOn))
        return fixedFlashlightState(isFlashLightOn)

    return transitionToNextFlashlightState(currentState, keyEvent)
}


/**
 * Our state can get out of sync with the actual flashlight when the user manually turns the
 * flashlight on or off outside of the app. If that happens, we update our state accordingly.
 */
private fun isStateCongruentWithFlashLight(currentState: FlashLightState, isFlashLightOn: Boolean): Boolean {
    return when (currentState to isFlashLightOn) {
        FlashLightState.OFF         to true  -> false  // Flash is on but we don't know it
        FlashLightState.ON_SWITCHED to false -> false  // Flash is off but we don't know it
        else -> true
    }
}


private fun fixedFlashlightState(isFlashLightOn: Boolean): FlashLightState {
    return if (isFlashLightOn) FlashLightState.ON_SWITCHED else FlashLightState.OFF
}


private fun transitionToNextFlashlightState(currentState: FlashLightState, keyEvent: PressableKeyState): FlashLightState {
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
