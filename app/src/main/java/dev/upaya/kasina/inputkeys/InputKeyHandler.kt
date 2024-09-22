package dev.upaya.kasina.inputkeys

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combineTransform
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InputKeyHandler @Inject constructor() {

    private val volumeDownKey = PressableKey()
    private val volumeUpKey = PressableKey()

    private val volumeDownKeyState = volumeDownKey.keyState
    private val volumeUpKeyState = volumeUpKey.keyState
    val volumeKeysState = volumeDownKeyState.combineTransform(volumeUpKeyState) { down, up ->
        // TODO merge buttons
        emit(down)
    }

    fun handleVolumeDownPress(scope: CoroutineScope) {
        volumeDownKey.press(scope = scope)
    }

    fun handleVolumeUpPress(scope: CoroutineScope) {
        volumeUpKey.press(scope = scope)
    }

    fun handleVolumeDownRelease() {
        volumeDownKey.release()
    }

    fun handleVolumeUpRelease() {
        volumeUpKey.release()
    }
}
