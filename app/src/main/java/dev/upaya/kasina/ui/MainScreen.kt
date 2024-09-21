package dev.upaya.kasina.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.upaya.kasina.R
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@Composable
fun MainScreen() {

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(12, 12, 12))
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(.5f),
                colorFilter = ColorFilter.tint(Color(192, 37, 88)),
                painter = painterResource(R.drawable.baseline_lightbulb_circle_24),
                contentDescription = "Lightbulb",
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
