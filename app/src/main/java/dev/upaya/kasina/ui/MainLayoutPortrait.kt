package dev.upaya.kasina.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.upaya.kasina.data.Session
import dev.upaya.kasina.data.SessionState
import dev.upaya.kasina.data.SessionState.INACTIVE


@Composable
internal fun MainLayoutPortrait(
    currentSession: Session?,
    sessionState: SessionState,
    recentSessions: List<Session>,
    modifier: Modifier = Modifier,
) {

    val sessionActiveAlpha: Float by animateFloatAsState(if (sessionState == INACTIVE) 1f else 0f, label = "alpha")

    Column(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
        .padding(18.dp)
    ) {

        Surface(
            shape = RoundedCornerShape(15),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(alpha = sessionActiveAlpha)
        ) {
            InfoText()
        }

        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            FlameAndTimer(
                sessionState = sessionState,
                currentSession = currentSession,
            )
        }

        Surface(
            shape = RoundedCornerShape(15),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            SessionStats(
                sessions = recentSessions,
                modifier = Modifier
                    .padding(18.dp)
            )
        }
    }
}
