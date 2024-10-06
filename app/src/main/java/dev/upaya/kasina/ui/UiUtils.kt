package dev.upaya.kasina.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import dev.upaya.kasina.data.Session
import dev.upaya.kasina.data.SessionState
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration


@Composable
internal fun SessionState.getColor(): Color =
    when (this) {
        SessionState.INACTIVE -> MaterialTheme.colorScheme.primaryContainer
        SessionState.ACTIVE_ON -> MaterialTheme.colorScheme.onTertiaryContainer
        SessionState.ACTIVE_OFF -> MaterialTheme.colorScheme.tertiaryContainer
    }


@Composable
internal fun SessionState.getNextColor(): Color =
    when (this) {
        SessionState.INACTIVE -> MaterialTheme.colorScheme.onTertiaryContainer
        SessionState.ACTIVE_ON -> MaterialTheme.colorScheme.tertiaryContainer
        SessionState.ACTIVE_OFF -> MaterialTheme.colorScheme.primaryContainer
    }


internal fun calcSessionDuration(currentSession: Session?): Duration {

    if (currentSession == null)
        return 0.toDuration(DurationUnit.SECONDS)

    if (currentSession.start == null)
        return 0.toDuration(DurationUnit.SECONDS)

    val durationSeconds = (Instant.now().toEpochMilli() - currentSession.start!!.toEpochMilli())

    return durationSeconds.toDuration(DurationUnit.MILLISECONDS)
}


internal suspend fun MutableInteractionSource.simulatePress() {
    val press = PressInteraction.Press(Offset.Zero)
    this.emit(press)
    this.emit(PressInteraction.Release(press))
}
