package dev.upaya.kasina.inputkeys

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


class PressableKey(private val longPressThresholdMillis: Long = 50L) {

    private val _keyState = MutableStateFlow(PressableKeyState.RELEASED)
    val keyState: SharedFlow<PressableKeyState> = _keyState

    private val isPressed
        get() = _keyState.value != PressableKeyState.RELEASED

    private val isReleased
        get() = !isPressed

    fun release() {

        if (isReleased)
            return

        _keyState.tryEmit(PressableKeyState.RELEASED)
    }

    fun press(scope: CoroutineScope) {

        if (isPressed)
            return

        _keyState.tryEmit(PressableKeyState.PRESSED_UNDECIDED)

        scope.launch {
            waitAndEmitShortOrLongPressEvent()
        }
    }

    private suspend fun waitAndEmitShortOrLongPressEvent() {
        delay(longPressThresholdMillis)
        if (isPressed) {
            _keyState.tryEmit(PressableKeyState.PRESSED_LONG)
        } else {
            _keyState.tryEmit(PressableKeyState.PRESSED_SHORT)
            _keyState.tryEmit(PressableKeyState.RELEASED)
        }
    }
}
