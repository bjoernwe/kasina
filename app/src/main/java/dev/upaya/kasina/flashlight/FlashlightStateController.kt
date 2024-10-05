package dev.upaya.kasina.flashlight

import dev.upaya.kasina.flashlight.FlashlightState.OFF
import dev.upaya.kasina.flashlight.FlashlightState.OFF_IN_SESSION
import dev.upaya.kasina.flashlight.FlashlightState.TRANSITION_TO_ON
import dev.upaya.kasina.flashlight.FlashlightState.TRANSITION_TO_OFF
import dev.upaya.kasina.flashlight.FlashlightState.ON_HOLDING
import dev.upaya.kasina.flashlight.FlashlightState.ON_SWITCHED
import dev.upaya.kasina.inputkeys.PressableButtonState.RELEASED
import dev.upaya.kasina.inputkeys.PressableButtonState.PRESSED
import dev.upaya.kasina.inputkeys.PressableButtonState.PRESSED_LONG
import dev.upaya.kasina.inputkeys.InputKeyHandler
import dev.upaya.kasina.inputkeys.PressableButtonState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FlashlightStateController @Inject constructor(
    private val flashlight: Flashlight,
    private val inputKeyHandler: InputKeyHandler,
) {

    private var _flashlightState = MutableStateFlow(OFF)
    val flashlightState: StateFlow<FlashlightState> = _flashlightState

    suspend fun startControllingFlashlightState(dispatcher: CoroutineDispatcher = Dispatchers.Default) {
        withContext(dispatcher) {
            launch { launchFlashlightOnOffJob() }
            launch { launchFlashlightStateJob() }
        }
    }

    fun turnOff() {
        _flashlightState.update { OFF }
    }

    /**
     * This job subscribes to our own flashlight state and turns the actual flashlight on/off
     * accordingly.
     */
    private suspend fun launchFlashlightOnOffJob() {
        _flashlightState.collect { state ->
            when (state) {
                OFF            -> flashlight.turnOff()
                OFF_IN_SESSION -> flashlight.turnOff()
                else           -> flashlight.turnOn()
            }
        }
    }

    /**
     * This jobs keeps the flashlight state updated according to the incoming events.
     */
    private suspend fun launchFlashlightStateJob() {
        _flashlightState.updateFlashlightStateOnInputEvents(inputKeyHandler.buttonState, flashlight.events) { currentState, keyEvent, isFlashlightOn ->
            calcFlashlightState(currentState, keyEvent, isFlashlightOn)
        }
    }
}


private fun calcFlashlightState(currentState: FlashlightState, keyEvent: PressableButtonState, isFlashlightOn: Boolean): FlashlightState {

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
        OFF         to true  -> false  // Flash is on but we don't know it
        ON_SWITCHED to false -> false  // Flash is off but we don't know it
        else -> true
    }
}


private fun getFixedFlashlightState(isFlashlightOn: Boolean): FlashlightState {
    return if (isFlashlightOn) ON_SWITCHED else OFF
}


private fun calcNextFlashlightState(currentState: FlashlightState, keyEvent: PressableButtonState): FlashlightState {
    return when (currentState to keyEvent) {
    //  currentState      && keyEvent     -> newState
        OFF               to PRESSED      -> TRANSITION_TO_ON
        OFF               to PRESSED_LONG -> ON_HOLDING
        TRANSITION_TO_ON  to PRESSED_LONG -> ON_HOLDING
        TRANSITION_TO_ON  to RELEASED     -> ON_SWITCHED
        ON_SWITCHED       to PRESSED      -> TRANSITION_TO_OFF
        TRANSITION_TO_OFF to PRESSED_LONG -> ON_HOLDING
        TRANSITION_TO_OFF to RELEASED     -> OFF_IN_SESSION
        ON_HOLDING        to RELEASED     -> OFF_IN_SESSION
        OFF_IN_SESSION    to PRESSED      -> OFF
        else -> currentState
    }
}
