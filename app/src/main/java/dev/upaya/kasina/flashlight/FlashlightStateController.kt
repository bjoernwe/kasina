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
class FlashlightStateController @Inject constructor(
    private val flashlight: Flashlight,
    private val inputKeyHandler: InputKeyHandler,
) {

    val isFlashlightOn = flashlight.isOn

    private var _flashlightState = MutableStateFlow(FlashlightState.OFF)

    private var flashlightStateJob: Job? = null
    private var flashlightOnOffJob: Job? = null

    fun start(scope: CoroutineScope) {
        flashlightOnOffJob = launchFlashlightOnOffJob(scope)
        flashlightStateJob = launchFlashlightStateJob(scope)
    }

    fun turnOff() {
        _flashlightState.update { FlashlightState.OFF }
    }

    /**
     * This job subscribes to our own flashlight state and turns the actual flashlight on/off
     * accordingly.
     */
    private fun launchFlashlightOnOffJob(scope: CoroutineScope) = scope.launch {
        _flashlightState.collect { state ->
            when (state) {
                FlashlightState.OFF -> flashlight.turnOff()
                else -> flashlight.turnOn()
            }
        }
    }

    /**
     * This jobs keeps the flashlight state updated according to the incoming events.
     */
    private fun launchFlashlightStateJob(scope: CoroutineScope) = scope.launch {
        _flashlightState.updateFlashlightStateOnInputEvents(inputKeyHandler.volumeKeysState, flashlight.isOn) { currentState, keyEvent, isFlashlightOn ->
            calcFlashlightState(currentState, keyEvent, isFlashlightOn)
        }
    }
}


private fun calcFlashlightState(currentState: FlashlightState, keyEvent: PressableKeyState, isFlashlightOn: Boolean): FlashlightState {

    // Did the user turn the flashlight on/off manually?
    if (!isStateCongruentWithFlashlight(currentState, isFlashlightOn))
        return getFixedFlashlightState(isFlashlightOn)

    return calcNextFlashlightState(currentState, keyEvent)
}


/**
 * Our state can get out of sync with the actual flashlight when the user manually turns the
 * flashlight on or off outside of the app. If that happens, we update our state accordingly.
 */
private fun isStateCongruentWithFlashlight(currentState: FlashlightState, isFlashlightOn: Boolean): Boolean {
    return when (currentState to isFlashlightOn) {
        FlashlightState.OFF         to true  -> false  // Flash is on but we don't know it
        FlashlightState.ON_SWITCHED to false -> false  // Flash is off but we don't know it
        else -> true
    }
}


private fun getFixedFlashlightState(isFlashlightOn: Boolean): FlashlightState {
    return if (isFlashlightOn) FlashlightState.ON_SWITCHED else FlashlightState.OFF
}


private fun calcNextFlashlightState(currentState: FlashlightState, keyEvent: PressableKeyState): FlashlightState {
    return when (currentState to keyEvent) {
        FlashlightState.OFF               to PressableKeyState.PRESSED      -> FlashlightState.TRANSITION_TO_ON
        FlashlightState.TRANSITION_TO_ON  to PressableKeyState.PRESSED_LONG -> FlashlightState.ON_HOLDING
        FlashlightState.TRANSITION_TO_ON  to PressableKeyState.RELEASED     -> FlashlightState.ON_SWITCHED
        FlashlightState.ON_SWITCHED       to PressableKeyState.PRESSED      -> FlashlightState.TRANSITION_TO_OFF
        FlashlightState.TRANSITION_TO_OFF to PressableKeyState.PRESSED_LONG -> FlashlightState.ON_HOLDING
        FlashlightState.TRANSITION_TO_OFF to PressableKeyState.RELEASED     -> FlashlightState.OFF
        FlashlightState.ON_HOLDING        to PressableKeyState.RELEASED     -> FlashlightState.OFF
        else -> currentState
    }
}
