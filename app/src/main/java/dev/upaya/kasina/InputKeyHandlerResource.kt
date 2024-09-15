package dev.upaya.kasina

import android.content.Context
import kotlinx.coroutines.CoroutineScope


class InputKeyHandlerResource(appContext: Context) : AutoCloseable {

    private val volumeDownKey = PressableKey(longPressThresholdMillis = 10L)

    private val flashLightControllerResource = FlashLightControllerResource(appContext)

    fun handleVolumeDownPress(
        shortPressCallback: () -> Unit,
        longPressCallback: () -> Unit,
        scope: CoroutineScope,
    ) {
        volumeDownKey.press(
            shortPressCallback = shortPressCallback,
            longPressCallback = longPressCallback,
            scope = scope,
        )
    }

    /**
     * @return the previous press state
     */
    fun handleVolumeDownRelease(): PressState {
        return volumeDownKey.release()
    }

    override fun close() {
        flashLightControllerResource.close()
    }
}
