package dev.upaya.kasina.inputkeys

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


class PressableKey(private val longPressThresholdMillis: Long) {

    private val _keyState = MutableSharedFlow<PressableKeyState>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val keyState: SharedFlow<PressableKeyState> = _keyState

    private var pressState = PressableKeyState.RELEASED

    private val isPressed
        get() = pressState != PressableKeyState.RELEASED

    private val isReleased
        get() = !isPressed

    fun release() {

        if (isReleased)
            return

        pressState = PressableKeyState.RELEASED
        _keyState.tryEmit(PressableKeyState.RELEASED)
    }

    fun press(scope: CoroutineScope) {

        if (isPressed)
            return

        pressState = PressableKeyState.SHORT_PRESSED

        scope.launch {
            pressState = waitAndEmitShortOrLongPressEvent()
        }
    }

    private suspend fun waitAndEmitShortOrLongPressEvent(): PressableKeyState {
        delay(longPressThresholdMillis)
        return if (isPressed) {
            _keyState.emit(PressableKeyState.LONG_PRESSED)
            PressableKeyState.LONG_PRESSED
        } else {
            _keyState.emit(PressableKeyState.SHORT_PRESSED)
            _keyState.emit(PressableKeyState.RELEASED)
            PressableKeyState.RELEASED
        }
    }
}
