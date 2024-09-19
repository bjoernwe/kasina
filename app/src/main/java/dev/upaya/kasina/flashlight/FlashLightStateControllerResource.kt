package dev.upaya.kasina.flashlight

import android.content.Context
import dev.upaya.kasina.inputkeys.InputKeyHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class FlashLightStateControllerResource @Inject constructor(
    private val inputKeyHandler: InputKeyHandler,
) : AutoCloseable {

    private var flashLightState = FlashLightState.OFF
    private var flashLightResource: FlashLightResource? = null

    private var collectReleaseEventsJob: Job? = null
    private var collectLongPressEventsJob: Job? = null
    private var collectShortPressEventsJob: Job? = null

    fun start(context: Context, scope: CoroutineScope) {
        flashLightResource = FlashLightResource(context)
        collectReleaseEventsJob = scope.launch { collectVolumeReleaseEvents() }
        collectLongPressEventsJob = scope.launch { collectVolumeLonPressEvents() }
        collectShortPressEventsJob = scope.launch { collectVolumeShortPressEvents() }
    }

    override fun close() {
        turnOff()
        collectReleaseEventsJob?.cancel()
        collectLongPressEventsJob?.cancel()
        collectShortPressEventsJob?.cancel()
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

    private suspend fun collectVolumeShortPressEvents() {
        inputKeyHandler.volumeKeyShortPressed.collect {
            if (flashLightState == FlashLightState.OFF)
                switchOn()
            else if (flashLightState == FlashLightState.ON_SWITCHED)
                turnOff()
        }
    }

    private suspend fun collectVolumeLonPressEvents() {
        inputKeyHandler.volumeKeyLongPressed.collect {
            if (flashLightState == FlashLightState.OFF || flashLightState == FlashLightState.ON_SWITCHED)
                holdOn()
        }
    }

    private suspend fun collectVolumeReleaseEvents() {
        inputKeyHandler.volumeKeyReleased.collect {
            if (flashLightState == FlashLightState.ON_HOLD)
                turnOff()
        }
    }
}
