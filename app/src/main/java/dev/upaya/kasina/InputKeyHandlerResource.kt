package dev.upaya.kasina

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class InputKeyHandlerResource(appContext: Context) : AutoCloseable {

    private val volumeDownKey = PressableKey(longPressThresholdMillis = 10L)

    private val flashLightControllerResource = FlashLightControllerResource(appContext)

    fun handleVolumeDownPress(scope: CoroutineScope) {
        volumeDownKey.press(
            shortPressCallback = { handleShortPress() },
            longPressCallback = { handleLongPress(scope) },
            scope = scope,
        )
    }

    fun handleVolumeDownRelease() {
        volumeDownKey.release()
    }

    private fun handleShortPress() {
        flashLightControllerResource.toggle()
    }

    private fun handleLongPress(scope: CoroutineScope) {
        scope.launch {
            flashLightControllerResource.turnOnFor(timeMillis = 1000)
        }
    }

    override fun close() {
        flashLightControllerResource.close()
    }
}
