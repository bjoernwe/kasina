package dev.upaya.kasina.inputkeys

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


class PressableButton(private val longPressThresholdMillis: Long = 200L) {

    private val _buttonState = MutableStateFlow(PressableButtonState.RELEASED)
    val buttonState: SharedFlow<PressableButtonState> = _buttonState

    private val isPressed
        get() = _buttonState.value != PressableButtonState.RELEASED

    private val isReleased
        get() = !isPressed

    fun release() {

        if (isReleased)
            return

        _buttonState.tryEmit(PressableButtonState.RELEASED)
    }

    fun press(scope: CoroutineScope) {

        if (isPressed)
            return

        _buttonState.tryEmit(PressableButtonState.PRESSED)

        scope.launch {
            waitAndEmitShortOrLongPressEvent()
        }
    }

    private suspend fun waitAndEmitShortOrLongPressEvent() {
        delay(longPressThresholdMillis)
        if (isPressed) {
            _buttonState.value = PressableButtonState.PRESSED_LONG
        } else {
            _buttonState.value = PressableButtonState.RELEASED
        }
    }
}
