package dev.upaya.kasina

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InputKeyHandler @Inject constructor() {

    private val volumeDownKey = PressableKey(longPressThresholdMillis = 25L)

    val volumeKeyShortPressed: SharedFlow<Instant> = volumeDownKey.shortPressed
    val volumeKeyLongPressed: SharedFlow<Instant> = volumeDownKey.longPressed
    val volumeKeyReleased: SharedFlow<Instant> = volumeDownKey.released

    fun handleVolumePress(scope: CoroutineScope) {
        volumeDownKey.press(scope = scope)
    }

    fun handleVolumeRelease() {
        volumeDownKey.release()
    }
}
