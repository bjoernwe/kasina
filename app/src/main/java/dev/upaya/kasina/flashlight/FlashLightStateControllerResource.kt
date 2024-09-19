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

    private fun holdOn() {
        flashLightResource?.turnOn()
        flashLightState = FlashLightState.ON_HOLD
    }

    private fun switchOn() {
        flashLightResource?.turnOn()
        flashLightState = FlashLightState.ON_SWITCHED
    }

    private suspend fun collectVolumeKeyEvents() {
        inputKeyHandler.volumeKeyState.collect { state ->
            when (state) {
                PressableKeyState.RELEASED -> { turnOffIfCurrentlyHolding() }
                PressableKeyState.SHORT_PRESSED -> { switchOnOrOff() }
                PressableKeyState.LONG_PRESSED -> { turnOnHolding() }
            }
        }
    }

    private fun turnOffIfCurrentlyHolding() {
        if (flashLightState == FlashLightState.ON_HOLD)
            turnOff()
    }

    private fun switchOnOrOff() {
        if (flashLightState == FlashLightState.OFF)
            switchOn()
        else //if (flashLightState == FlashLightState.ON_SWITCHED)
            turnOff()
    }

    private fun turnOnHolding() {
        if (flashLightState == FlashLightState.OFF ||
            flashLightState == FlashLightState.ON_SWITCHED
        )
            holdOn()
    }
}
