package dev.upaya.kasina.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.upaya.kasina.R
import dev.upaya.kasina.data.SessionState
import dev.upaya.kasina.flashlight.FlashlightViewModel
import dev.upaya.kasina.ui.theme.FlashKasinaTheme
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.DurationUnit
import kotlin.time.toDuration


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val flashlightViewModel: FlashlightViewModel = hiltViewModel()
    val recentSessions by flashlightViewModel.recentSessions.collectAsState(initial = emptyList())
    val sessionState by flashlightViewModel.sessionState.collectAsState(SessionState.INACTIVE)
    val currentSession by flashlightViewModel.currentSession.collectAsState(initial = null)

    var sessionDuration by remember { mutableStateOf(0.toDuration(DurationUnit.SECONDS)) }

    LaunchedEffect(currentSession) {
        while (true) {
            sessionDuration = calcSessionDuration(currentSession)
            delay(100L)
        }
    }

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
            .padding(24.dp)
        ) {

            Row(
                modifier = Modifier
                    .wrapContentHeight(),
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_info_24),
                    contentDescription = "Info",
                    tint = if (sessionState == SessionState.INACTIVE) MaterialTheme.colorScheme.onSurfaceVariant else Color.Transparent,
                )
                Spacer(
                    modifier = Modifier.padding(4.dp),
                )
                Text(
                    text = if (sessionState == SessionState.INACTIVE) "Press/hold physical buttons to start" else "",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    FlameIcon(
                        sessionState = sessionState,
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(
                        modifier = Modifier.padding(8.dp),
                    )
                    Text(
                        text = if (sessionState == SessionState.ACTIVE_OFF) String.format(
                            Locale.ROOT,"%02d:%02d:%02d",
                            sessionDuration.inWholeHours,
                            sessionDuration.inWholeMinutes % 60,
                            sessionDuration.inWholeSeconds % 60,
                        ) else "",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            SessionStats(
                sessions = recentSessions,
                modifier = Modifier
                    .weight(1f),
            )
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
