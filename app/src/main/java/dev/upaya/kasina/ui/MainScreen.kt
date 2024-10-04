package dev.upaya.kasina.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.upaya.kasina.R
import dev.upaya.kasina.data.Session
import dev.upaya.kasina.data.SessionState
import dev.upaya.kasina.flashlight.FlashlightViewModel
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@Composable
fun MainScreen() {

    val configuration = LocalConfiguration.current
    val isLandscape = remember {
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    val flashlightViewModel: FlashlightViewModel = hiltViewModel()
    val recentSessions: State<List<Session>> = flashlightViewModel.recentSessions.collectAsState(initial = emptyList())
    val sessionState = flashlightViewModel.sessionState.collectAsState(SessionState.INACTIVE)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

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
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(
                    modifier = Modifier.padding(4.dp),
                )
                Text(
                    text = "Press or hold volume button to turn flashlight on/off",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_lightbulb_circle_24),
                    modifier = Modifier
                        .fillMaxSize(.33f)
                        .aspectRatio(1f, !isLandscape),
                    contentDescription = "Lightbulb",
                    tint = when (sessionState.value) {
                        SessionState.INACTIVE -> MaterialTheme.colorScheme.primaryContainer
                        SessionState.ACTIVE_ON -> MaterialTheme.colorScheme.onTertiaryContainer
                        SessionState.ACTIVE_OFF -> MaterialTheme.colorScheme.tertiaryContainer
                    },
                )
            }

            SessionStats(
                sessions = recentSessions.value,
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
