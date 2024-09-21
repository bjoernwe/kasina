package dev.upaya.kasina.flashlight

import android.content.Context
import dev.upaya.kasina.inputkeys.InputKeyHandler
import dev.upaya.kasina.inputkeys.PressableKeyState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class FlashLightStateControllerResource @Inject constructor(
    private val inputKeyHandler: InputKeyHandler,
) : AutoCloseable {

    private var flashLightState = FlashLightState.OFF
    private var flashLightResource: FlashLightResource? = null

    private var collectVolumeKeyEventsJob: Job? = null

    fun start(context: Context, scope: CoroutineScope) {
        flashLightResource = FlashLightResource(context)
        collectVolumeKeyEventsJob = scope.launch { collectVolumeKeyEvents() }
    }

    override fun close() {
        turnOff()
        collectVolumeKeyEventsJob?.cancel()
        flashLightResource?.close()
    }

    private fun turnOff() {
        flashLightResource?.turnOff()
        flashLightState = FlashLightState.OFF
    }

    private fun turnOnUndecided() {
        flashLightResource?.turnOn()
        flashLightState = FlashLightState.ON_UNDECIDED
    }

    private fun turnOnHolding() {
        flashLightResource?.turnOn()
        flashLightState = FlashLightState.ON_HOLDING
    }

    private fun turnOnSwitched() {
        flashLightResource?.turnOn()
        flashLightState = FlashLightState.ON_SWITCHED
    }

    private suspend fun collectVolumeKeyEvents() {
        inputKeyHandler.volumeKeyState.collect { state ->
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
            FlashLightState.OFF -> { /* nop */ }
            FlashLightState.ON_UNDECIDED -> { /* nop */ }
            FlashLightState.ON_SWITCHED -> { /* nop */ }
            FlashLightState.ON_HOLDING -> { turnOff() }
        }
    }

    private fun handleUndecidedButtonPress() {
        when (flashLightState) {
            FlashLightState.OFF -> { turnOnUndecided() }
            FlashLightState.ON_UNDECIDED -> { /* nop */ }
            FlashLightState.ON_SWITCHED -> { /* nop */ }
            FlashLightState.ON_HOLDING -> { /* nop */ }
        }
    }

    private fun handleShortPress() {
        when (flashLightState) {
            FlashLightState.OFF -> { /* nop */ }
            FlashLightState.ON_UNDECIDED -> { turnOnSwitched() }
            FlashLightState.ON_SWITCHED -> { turnOff() }
            FlashLightState.ON_HOLDING -> { /* nop */ }
        }
    }

    private fun handleLongPress() {
        when (flashLightState) {
            FlashLightState.OFF -> { /* nop */ }
            FlashLightState.ON_UNDECIDED -> { turnOnHolding() }
            FlashLightState.ON_SWITCHED -> { /* nop */ }
            FlashLightState.ON_HOLDING -> { /* nop */ }
        }
    }
}
