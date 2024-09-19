package dev.upaya.kasina

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.Instant


enum class PressState {
    RELEASED,
    SHORT_PRESSED,
    LONG_PRESSED,
}


class PressableKey(private val longPressThresholdMillis: Long) {

    private val _shortPressed = MutableSharedFlow<Instant>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val _longPressed = MutableSharedFlow<Instant>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val _released = MutableSharedFlow<Instant>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val shortPressed: SharedFlow<Instant> = _shortPressed
    val longPressed: SharedFlow<Instant> = _longPressed
    val released: SharedFlow<Instant> = _released

    private var pressState = PressState.RELEASED

    private val isPressed
        get() = pressState != PressState.RELEASED

    private val isReleased
        get() = !isPressed

    fun release() {

        if (isReleased)
            return

        pressState = PressState.RELEASED
        _released.tryEmit(Instant.now())
    }

    fun press(scope: CoroutineScope) {

        if (isPressed)
            return

        pressState = PressState.SHORT_PRESSED

        scope.launch {
            pressState = waitAndEmitShortOrLongPressEvent()
        }
    }

    private suspend fun waitAndEmitShortOrLongPressEvent(): PressState {
        delay(longPressThresholdMillis)
        return if (isPressed) {
            _longPressed.tryEmit(Instant.now())
            PressState.LONG_PRESSED
        } else {
            _shortPressed.tryEmit(Instant.now())
            PressState.RELEASED
        }
    }
}
