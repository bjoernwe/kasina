package dev.upaya.kasina.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
@OptIn(ExperimentalMaterial3Api::class)
internal fun MainLayoutPortrait(
    currentSession: Session?,
    sessionState: SessionState,
    recentSessions: List<Session>,
    modifier: Modifier = Modifier,
) {

    val sessionActiveAlpha: Float by animateFloatAsState(if (sessionState == INACTIVE) 1f else 0f, label = "alpha")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Flash Kasina") },
            )
        },
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    .fillMaxWidth()
                    .weight(3f),
                contentAlignment = Alignment.Center,
            ) {
                FlameAndTimer(
                    sessionState = sessionState,
                    currentSession = currentSession,
                    modifier = Modifier
                        .fillMaxWidth(.4f)
                )
            }

            Surface(
                shape = RoundedCornerShape(15),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                modifier = Modifier
                    .weight(2f)
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
}
