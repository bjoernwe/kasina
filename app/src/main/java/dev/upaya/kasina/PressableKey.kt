package dev.upaya.kasina

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PressableKey(private val longPressThresholdMillis: Long) {

    private var pressed = false

    fun press(shortPressCallback: () -> Unit, longPressCallback: () -> Unit, scope: CoroutineScope) {

        if (pressed)
            return

        pressed = true

        scope.launch {
            waitAndCallCallback(shortPressCallback = shortPressCallback, longPressCallback = longPressCallback)
        }
    }

    fun release() {
        if (!pressed)
            return
        pressed = false
    }

    private suspend fun waitAndCallCallback(
        shortPressCallback: () -> Unit,
        longPressCallback: () -> Unit,
    ) {
        delay(longPressThresholdMillis)
        if (pressed) {
            longPressCallback()
        } else {
            shortPressCallback()
        }
    }
}
