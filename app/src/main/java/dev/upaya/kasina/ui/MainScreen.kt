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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.upaya.kasina.data.SessionState.INACTIVE
import dev.upaya.kasina.flashlight.FlashlightViewModel
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val flashlightViewModel: FlashlightViewModel = hiltViewModel()
    val recentSessions by flashlightViewModel.recentSessions.collectAsState(initial = emptyList())
    val sessionState by flashlightViewModel.sessionState.collectAsState(INACTIVE)
    val currentSession by flashlightViewModel.currentSession.collectAsState(initial = null)

    val sessionActiveAlpha: Float by animateFloatAsState(if (sessionState == INACTIVE) 1f else 0f, label = "alpha")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Flashlight Kasina") },
            )
        },
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->

        Column(modifier = Modifier
            .padding(innerPadding)
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
}


@Preview
@Composable
fun MainScreenPreview() {
    FlashKasinaTheme(darkTheme = true) {
        MainScreen()
    }
}
