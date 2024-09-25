package dev.upaya.kasina.flashlight

import dev.upaya.kasina.inputkeys.InputKeyHandler
import dev.upaya.kasina.inputkeys.PressableKeyState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FlashLightStateController @Inject constructor(
    private val flashLight: FlashLight,
    private val inputKeyHandler: InputKeyHandler,
) {

    val isFlashLightOn = flashLight.isOn

    private var flashLightState = FlashLightState.OFF

    private var collectVolumeKeyEventsJob: Job? = null

    fun start(scope: CoroutineScope) {
        collectVolumeKeyEventsJob = scope.launch { collectVolumeKeyEvents() }
    }

    fun turnOff() {
        flashLight.turnOff()
        flashLightState = FlashLightState.OFF
    }

    private fun transitionToOn() {
        flashLight.turnOn()
        flashLightState = FlashLightState.TRANSITION_TO_ON
    }

    private fun transitionToOff() {
        flashLightState = FlashLightState.TRANSITION_TO_OFF
    }

    private fun turnOnHolding() {
        flashLightState = FlashLightState.ON_HOLDING
    }

    private fun turnOnSwitched() {
        flashLightState = FlashLightState.ON_SWITCHED
    }

    private suspend fun collectVolumeKeyEvents() {
        inputKeyHandler.volumeKeysState.collect { state ->
            when (state) {
                PressableKeyState.RELEASED -> { handleButtonRelease() }
                PressableKeyState.PRESSED -> { handleButtonPress() }
                PressableKeyState.PRESSED_LONG -> { handleButtonLongPress() }
            }
        }
    }

    private fun handleButtonRelease() {
        when (flashLightState) {
            FlashLightState.OFF -> { /* NOP */ }
            FlashLightState.TRANSITION_TO_ON -> { turnOnSwitched() }
            FlashLightState.TRANSITION_TO_OFF -> { turnOff() }
            FlashLightState.ON_SWITCHED -> { /* NOP */ }
            FlashLightState.ON_HOLDING -> { turnOff() }
        }
    }

    private fun handleButtonPress() {
        when (flashLightState) {
            FlashLightState.OFF -> { transitionToOn() }
            FlashLightState.TRANSITION_TO_ON -> { /* NOP */ }
            FlashLightState.TRANSITION_TO_OFF -> { /* NOP */ }
            FlashLightState.ON_SWITCHED -> { transitionToOff() }
            FlashLightState.ON_HOLDING -> { /* NOP */ }
        }
    }

    private fun handleButtonLongPress() {
        when (flashLightState) {
            FlashLightState.OFF -> { /* NOP */ }
            FlashLightState.TRANSITION_TO_ON -> { turnOnHolding() }
            FlashLightState.TRANSITION_TO_OFF -> { turnOnHolding() }
            FlashLightState.ON_SWITCHED -> { /* NOP */ }
            FlashLightState.ON_HOLDING -> { /* NOP */ }
        }
    }
}
