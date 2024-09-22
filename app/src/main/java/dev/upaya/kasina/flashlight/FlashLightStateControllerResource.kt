package dev.upaya.kasina.flashlight

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.upaya.kasina.inputkeys.InputKeyHandler
import dev.upaya.kasina.inputkeys.PressableKeyState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
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

    fun turnOff() {
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
