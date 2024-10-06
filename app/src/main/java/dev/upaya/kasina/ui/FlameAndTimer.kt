package dev.upaya.kasina.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.upaya.kasina.data.Session
import dev.upaya.kasina.data.SessionState
import dev.upaya.kasina.data.SessionState.ACTIVE_OFF
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.DurationUnit
import kotlin.time.toDuration


@Composable
internal fun FlameAndTimer(
    sessionState: SessionState,
    currentSession: Session?,
    modifier: Modifier = Modifier,
) {
    var sessionDuration by remember { mutableStateOf(0.toDuration(DurationUnit.SECONDS)) }

    LaunchedEffect(currentSession) {
        while (true) {
            sessionDuration = calcSessionDuration(currentSession)
            delay(100L)
        }
    }

    Column(
        modifier = modifier
    ) {
        FlameIcon(
            sessionState = sessionState,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(
            modifier = Modifier.padding(8.dp),
        )
        Text(
            text = if (sessionState == ACTIVE_OFF) String.format(
                Locale.ROOT,"%02d:%02d:%02d",
                sessionDuration.inWholeHours,
                sessionDuration.inWholeMinutes % 60,
                sessionDuration.inWholeSeconds % 60,
            ) else "",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally)
        )
    }
}
