package dev.upaya.kasina.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import dev.upaya.kasina.flashlight.FlashlightViewModel
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@Composable
fun MainScreen() {

    val configuration = LocalConfiguration.current
    val isLandscape = remember {
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    val flashlightViewModel: FlashlightViewModel = hiltViewModel()
    val isFlashlightOn = flashlightViewModel.isFlashlightOn.collectAsState(false)
    val flashlightEvents = flashlightViewModel.flashlightEvents.collectAsState(initial = emptyList())

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(24.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                items(flashlightEvents.value) { event ->
                    Text(event.timestamp.toString())
                }
            }
            Icon(
                painter = painterResource(R.drawable.baseline_lightbulb_circle_24),
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(.25f)
                    .aspectRatio(1f, !isLandscape),
                contentDescription = "Lightbulb",
                tint = if (isFlashlightOn.value) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart),
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_info_24),
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.onBackground,
                    )
                Spacer(
                    modifier = Modifier.padding(4.dp),
                )
                Text(
                    text = "Press or hold volume buttons to turn flashlight on/off",
                    color = MaterialTheme.colorScheme.onBackground,
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
