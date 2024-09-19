package dev.upaya.kasina.inputkeys

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.Instant


class PressableKey(private val longPressThresholdMillis: Long) {

    private val _shortPressed = MutableSharedFlow<Instant>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val _longPressed = MutableSharedFlow<Instant>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val _released = MutableSharedFlow<Instant>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val shortPressed: SharedFlow<Instant> = _shortPressed
    val longPressed: SharedFlow<Instant> = _longPressed
    val released: SharedFlow<Instant> = _released

    private var pressableKeyState = PressableKeyState.RELEASED

    private val isPressed
        get() = pressableKeyState != PressableKeyState.RELEASED

    private val isReleased
        get() = !isPressed

    fun release() {

        if (isReleased)
            return

        pressableKeyState = PressableKeyState.RELEASED
        _released.tryEmit(Instant.now())
    }

    fun press(scope: CoroutineScope) {

        if (isPressed)
            return

        pressableKeyState = PressableKeyState.SHORT_PRESSED

        scope.launch {
            pressableKeyState = waitAndEmitShortOrLongPressEvent()
        }
    }

    private suspend fun waitAndEmitShortOrLongPressEvent(): PressableKeyState {
        delay(longPressThresholdMillis)
        return if (isPressed) {
            _longPressed.tryEmit(Instant.now())
            PressableKeyState.LONG_PRESSED
        } else {
            _shortPressed.tryEmit(Instant.now())
            PressableKeyState.RELEASED
        }
    }
}
