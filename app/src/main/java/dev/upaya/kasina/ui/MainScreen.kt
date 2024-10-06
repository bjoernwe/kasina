package dev.upaya.kasina.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Flashlight Kasina") },
            )
        },
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->

        MainLayoutPortrait(
            currentSession = currentSession,
            sessionState = sessionState,
            recentSessions = recentSessions,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}


@Preview
@Composable
fun MainScreenPreview() {
    FlashKasinaTheme(darkTheme = true) {
        MainScreen()
    }
}
