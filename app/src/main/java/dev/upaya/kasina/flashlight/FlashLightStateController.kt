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
    val flashLight: FlashLight,
    private val inputKeyHandler: InputKeyHandler,
) {

    private var flashLightState = FlashLightState.OFF
    private var collectVolumeKeyEventsJob: Job? = null

    fun start(scope: CoroutineScope) {
        collectVolumeKeyEventsJob = scope.launch { collectVolumeKeyEvents() }
    }

    fun turnOff() {
        flashLight.turnOff()
        flashLightState = FlashLightState.OFF
    }

    private fun turnOnUndecided() {
        flashLight.turnOn()
        flashLightState = FlashLightState.ON_UNDECIDED
    }

    private fun turnOnHolding() {
        flashLight.turnOn()
        flashLightState = FlashLightState.ON_HOLDING
    }

    private fun turnOnSwitched() {
        flashLight.turnOn()
        flashLightState = FlashLightState.ON_SWITCHED
    }

    private suspend fun collectVolumeKeyEvents() {
        inputKeyHandler.volumeKeysState.collect { state ->
            when (state) {
                PressableKeyState.RELEASED -> { handleButtonRelease() }
                PressableKeyState.PRESSED_UNDECIDED -> { handleUndecidedButtonPress() }
                PressableKeyState.PRESSED_SHORT -> { handleShortPress() }
                PressableKeyState.PRESSED_LONG -> { handleLongPress() }
            }
        }
    }

    private fun handleButtonRelease() {
        when (flashLightState) {
            FlashLightState.OFF -> { /* NOP */ }
            FlashLightState.ON_UNDECIDED -> { /* NOP */ }
            FlashLightState.ON_SWITCHED -> { /* NOP */ }
            FlashLightState.ON_HOLDING -> { turnOff() }
        }
    }

    private fun handleUndecidedButtonPress() {
        when (flashLightState) {
            FlashLightState.OFF -> { turnOnUndecided() }
            FlashLightState.ON_UNDECIDED -> { /* NOP */ }
            FlashLightState.ON_SWITCHED -> { /* NOP */ }
            FlashLightState.ON_HOLDING -> { /* NOP */ }
        }
    }

    private fun handleShortPress() {
        when (flashLightState) {
            FlashLightState.OFF -> { turnOnSwitched() }
            FlashLightState.ON_UNDECIDED -> { turnOnSwitched() }
            FlashLightState.ON_SWITCHED -> { turnOff() }
            FlashLightState.ON_HOLDING -> { /* NOP */ }
        }
    }

    private fun handleLongPress() {
        when (flashLightState) {
            FlashLightState.OFF -> { turnOnHolding() }
            FlashLightState.ON_UNDECIDED -> { turnOnHolding() }
            FlashLightState.ON_SWITCHED -> { turnOnHolding() /* override SWITCHED with HOLD */ }
            FlashLightState.ON_HOLDING -> { /* NOP */ }
        }
    }
}
