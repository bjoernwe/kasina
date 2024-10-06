package dev.upaya.kasina.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import dev.upaya.kasina.data.SessionState


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


internal suspend fun MutableInteractionSource.simulatePress() {
    val press = PressInteraction.Press(Offset.Zero)
    this.emit(press)
    this.emit(PressInteraction.Release(press))
}
