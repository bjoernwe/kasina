package dev.upaya.kasina.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@Composable
fun MainScreen() {

    val viewModel: MainViewModel = viewModel()

    FlashKasinaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            TextButton(
                onClick = viewModel::toggleFlashLight,
                modifier = Modifier.padding(innerPadding)
            ) {
                Text(text = "Flash!")
            }
        }
    }
}
