package dev.upaya.kasina.inputkeys

import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InputKeyHandler @Inject constructor() {

    private val volumeDownKey = PressableKey()

    val volumeKeyState = volumeDownKey.keyState

    fun handleVolumePress(scope: CoroutineScope) {
        volumeDownKey.press(scope = scope)
    }

    fun handleVolumeRelease() {
        volumeDownKey.release()
    }
}
