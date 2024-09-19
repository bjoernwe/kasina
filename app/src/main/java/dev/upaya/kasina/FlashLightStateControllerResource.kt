package dev.upaya.kasina

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class FlashLightState {
    OFF, ON_SWITCHED, ON_HOLD,
}


class FlashLightStateControllerResource(
    context: Context,
    private val inputKeyHandler: InputKeyHandler,
    scope: CoroutineScope
) : AutoCloseable {

    private var flashLightState = FlashLightState.OFF
    private val flashLightResource = FlashLightResource(context)

    private var collectReleaseEventsJob: Job
    private var collectLongPressEventsJob: Job
    private var collectShortPressEventsJob: Job

    init {
        collectReleaseEventsJob = scope.launch { collectVolumeReleaseEvents() }
        collectLongPressEventsJob = scope.launch { collectVolumeLonPressEvents() }
        collectShortPressEventsJob = scope.launch { collectVolumeShortPressEvents() }
    }

    override fun close() {
        collectReleaseEventsJob.cancel()
        collectLongPressEventsJob.cancel()
        collectShortPressEventsJob.cancel()
        flashLightResource.close()
    }

    private fun turnOff() {
        flashLightResource.turnOff()
        flashLightState = FlashLightState.OFF
    }

    private fun holdOn() {
        flashLightResource.turnOn()
        flashLightState = FlashLightState.ON_HOLD
    }

    private fun switchOn() {
        flashLightResource.turnOn()
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
