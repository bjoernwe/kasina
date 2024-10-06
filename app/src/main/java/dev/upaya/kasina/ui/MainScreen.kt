package dev.upaya.kasina.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dev.upaya.kasina.data.SessionState.INACTIVE
import dev.upaya.kasina.flashlight.FlashlightViewModel
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@Composable
fun MainScreen() {

    val flashlightViewModel: FlashlightViewModel = hiltViewModel()
    val recentSessions by flashlightViewModel.recentSessions.collectAsState(initial = emptyList())
    val sessionState by flashlightViewModel.sessionState.collectAsState(INACTIVE)
    val currentSession by flashlightViewModel.currentSession.collectAsState(initial = null)

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

        if (isLandscape) {
            MainLayoutPortrait(
                currentSession = currentSession,
                sessionState = sessionState,
                recentSessions = recentSessions,
                modifier = Modifier
            )
        } else {
            MainLayoutLandscape(
                currentSession = currentSession,
                sessionState = sessionState,
                recentSessions = recentSessions,
                modifier = Modifier
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
