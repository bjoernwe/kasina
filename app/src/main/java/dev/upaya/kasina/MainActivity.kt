package dev.upaya.kasina

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.upaya.kasina.flashlight.FlashLightViewModel
import dev.upaya.kasina.ui.MainScreen
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var flashLightViewModel: FlashLightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        flashLightViewModel = ViewModelProvider(this)[FlashLightViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            FlashKasinaTheme(darkTheme = true, dynamicColor = false) {
                MainScreen()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK)
            return super.onKeyDown(keyCode, event)

        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> { flashLightViewModel.handleVolumeDownPress(lifecycleScope); true }
            KeyEvent.KEYCODE_VOLUME_UP -> { flashLightViewModel.handleVolumeUpPress(lifecycleScope); true }
            else -> { super.onKeyDown(keyCode, event) }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> { flashLightViewModel.handleVolumeDownRelease(); true }
            KeyEvent.KEYCODE_VOLUME_UP -> { flashLightViewModel.handleVolumeUpRelease(); true }
            else -> { super.onKeyUp(keyCode, event) }
        }
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStop() {
        super.onStop()
        if (!isChangingConfigurations) {
            flashLightViewModel.turnFlashOff()
        }
    }
}
