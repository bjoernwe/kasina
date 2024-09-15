package dev.upaya.kasina

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


enum class PressState {
    RELEASED,
    SHORT_PRESSED,
    LONG_PRESSED,
}


class PressableKey(private val longPressThresholdMillis: Long) {

    private var pressState = PressState.RELEASED

    private val pressed
        get() = pressState != PressState.RELEASED

    fun press(shortPressCallback: () -> Unit, longPressCallback: () -> Unit, scope: CoroutineScope) {

        if (pressed)
            return

        pressState = PressState.SHORT_PRESSED

        scope.launch {
            waitAndCallCallback(shortPressCallback = shortPressCallback, longPressCallback = longPressCallback)
        }
    }

    /**
     * @return the previous press state
     */
    fun release(): PressState {
        if (!pressed)
            return PressState.RELEASED
        val previousPressState = pressState
        pressState = PressState.RELEASED
        return previousPressState
    }

    private suspend fun waitAndCallCallback(
        shortPressCallback: () -> Unit,
        longPressCallback: () -> Unit,
    ) {
        delay(longPressThresholdMillis)
        if (pressed) {
            pressState = PressState.LONG_PRESSED
            longPressCallback()
        } else {
            shortPressCallback()
        }
    }
}
